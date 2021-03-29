package com.netty.trpc.client.handler;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.log.LOG;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 11:26
 */
public class TrpcFuture implements Future<Object> {
    private Sync sync;
    private TrpcRequest request;
    private TrpcResponse response;
    private long startTime;
    private long responseTimeThreshold = 5_000;
    private List<AsyncTrpcCallBack> pendingCallbacks = new LinkedList<>();
    private ReentrantLock lock = new ReentrantLock();

    public TrpcFuture(TrpcRequest request) {
        this.sync = new Sync();
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    public void done(TrpcResponse response){
        this.response = response;
        sync.release(1);
        invokeCallbacks();
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime>this.responseTimeThreshold){
            LOG.warn("Service response time is too slow. Request id="+response.getRequestId()+", response time="+responseTime+"ms");
        }else {
            LOG.info("Service response time is {}ms",responseTime);
        }
    }

    public TrpcFuture addCallback(AsyncTrpcCallBack callback) {
        lock.lock();
        try {
            if (isDone()) {
                doCallback(callback);
            } else {
                this.pendingCallbacks.add(callback);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    private void invokeCallbacks() {
        lock.lock();
        try {
            for (AsyncTrpcCallBack callback : pendingCallbacks) {
                doCallback(callback);
            }
        }finally {
            lock.unlock();
        }
    }

    private void doCallback(AsyncTrpcCallBack callback) {
        final TrpcResponse response = this.response;
        TrpcClient.submit(new Runnable() {
            @Override
            public void run() {
                if (response.isError()){
                    callback.fail(new RuntimeException("Response error,"+response.getError()));
                }else {
                    callback.success(response.getResult());
                }
            }
        });
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(1);
        if (this.response!=null){
            return this.response.getResult();
        }else {
            return null;
        }
    }

    public TrpcResponse getResponse() throws InterruptedException, ExecutionException {
        sync.acquire(1);
        if (this.response!=null){
            return this.response;
        }else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(1, unit.toNanos(timeout));
        if (success){
            if (this.response!=null){
                return this.response.getResult();
            }else {
                return null;
            }
        }
        throw new RuntimeException("Timeout exception, request id: "+request.getRequestId()+", request class name: "+request.getInterfaceName()+
                ", method: "+request.getMethodName()+", version: "+request.getVersion());
    }

    static class Sync extends AbstractQueuedSynchronizer{
        private final int done=1;
        private final int pending=0;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState()==done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState()==pending){
                return compareAndSetState(pending,done);
            }else {
                return true;
            }
        }

        protected boolean isDone(){
            return getState()==done;
        }
    }
}
