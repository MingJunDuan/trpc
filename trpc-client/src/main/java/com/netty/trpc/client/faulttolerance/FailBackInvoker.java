package com.netty.trpc.client.faulttolerance;

import com.netty.trpc.client.connect.ConnectionManager;
import com.netty.trpc.client.connect.ConnectionManagerFactory;
import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.handler.TrpcFuture;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.util.timer.HashedWheelTimer;
import com.netty.trpc.common.util.timer.Timeout;
import com.netty.trpc.common.util.timer.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * When failed, record request context and schedule for retry on a regular interval.
 * Especially useful for services of notification
 *
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-25 23:07
 */
public class FailBackInvoker implements Invoker{
    private static final Logger LOGGER = LoggerFactory.getLogger(FailBackInvoker.class);
    private static HashedWheelTimer hashedWheelTimer;
    private static int delay = 3;
    private static TimeUnit timeUnit = TimeUnit.SECONDS;

    @Override
    public Object invoke(String serviceKey, TrpcRequest request) {
        TrpcClientHandler handler = null;
        try {
            handler = ConnectionManagerFactory.getConnectionManager().chooseHandler(serviceKey);
            TrpcFuture trpcFuture = handler.sendRequest(request);
            return trpcFuture.get();
        } catch (Exception e) {
            LOGGER.debug("call service error, call again with Timer");
            CompletableFuture<Object> future = new CompletableFuture<>();

            retry(request,handler,future);
            try {
                return future.get(5,TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                LOGGER.error("Interrupted",e1);
            } catch (Exception e1) {
                LOGGER.error("Schedule services for retry exception",e1);
            }
        }
        return null;
    }

    private void retry(TrpcRequest request, TrpcClientHandler handler,CompletableFuture<Object> future) {
        if (hashedWheelTimer==null){
            synchronized (FailBackInvoker.class){
                if (hashedWheelTimer==null){
                    hashedWheelTimer = new HashedWheelTimer();
                }
            }
        }
        hashedWheelTimer.newTimeout(new FailRetryTimerTask(request,handler,future),delay, timeUnit);
    }


    static class FailRetryTimerTask implements TimerTask{
        private TrpcRequest trpcRequest;
        private TrpcClientHandler handler;
        private Object result;
        private CompletableFuture<Object> future;

        public FailRetryTimerTask(TrpcRequest trpcRequest, TrpcClientHandler handler,CompletableFuture<Object> future) {
            this.trpcRequest = trpcRequest;
            this.handler = handler;
            this.future = future;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            TrpcFuture trpcFuture = handler.sendRequest(trpcRequest);
            result = trpcFuture.get();
            future.complete(result);
        }
    }
}
