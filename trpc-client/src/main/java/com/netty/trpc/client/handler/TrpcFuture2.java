package com.netty.trpc.client.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.util.SystemClock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 11:26
 */
public class TrpcFuture2 implements Future<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcFuture2.class);
    private TrpcRequest request;
    private TrpcResponse response;
    private long startTime;
    private long responseTimeThreshold = 5_000;
    private List<AsyncTrpcCallBack> pendingCallbacks = new LinkedList<>();
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public TrpcFuture2(TrpcRequest request) {
        this.request = request;
        this.startTime = SystemClock.currentTimeMillis();
    }

    public void done(TrpcResponse response) {
        this.response = response;
        countDownLatch.countDown();
        invokeCallbacks();
        long responseTime = SystemClock.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            LOGGER.warn("Service response time is too slow. Request id=" + response.getRequestId() + ", response time=" + responseTime + "ms");
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Service response time, requestId:{}, consume: {}ms", request.getRequestId(), responseTime);
            }
        }
    }

    public TrpcFuture2 addCallback(AsyncTrpcCallBack callback) {
        if (isDone()) {
            doCallback(callback);
        } else {
            this.pendingCallbacks.add(callback);
        }
        return this;
    }

    private void invokeCallbacks() {
        for (AsyncTrpcCallBack callback : pendingCallbacks) {
            doCallback(callback);
        }
    }

    private void doCallback(AsyncTrpcCallBack callback) {
        final TrpcResponse response = this.response;
        TrpcClient.submit(new Runnable() {
            @Override
            public void run() {
                if (response.isError()) {
                    callback.fail(new RuntimeException("Response error," + response.getError()));
                } else {
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
        return countDownLatch.getCount() == 0;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        if (this.response != null) {
            return this.response.getResult();
        } else {
            return null;
        }
    }

    public TrpcResponse getResponse() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        if (this.response != null) {
            return this.response;
        } else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = countDownLatch.await(timeout, unit);
        if (success) {
            if (this.response != null) {
                return this.response.getResult();
            } else {
                return null;
            }
        }
        throw new RuntimeException("Timeout exception, request id: " + request.getRequestId() + ", request class name: " + request.getInterfaceName() +
                ", method: " + request.getMethodName() + ", version: " + request.getVersion());
    }
}
