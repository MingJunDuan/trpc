package com.netty.trpc.client.faulttolerance;

import com.netty.trpc.client.connect.ConnectionManagerFactory;
import com.netty.trpc.client.connect.ForkingConnectionManager;
import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.handler.TrpcFuture;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.util.threadpool.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-25 23:08
 */
public class ForkingInvoker implements Invoker{
    private static final Logger LOGGER = LoggerFactory.getLogger(ForkingInvoker.class);
    /**
     * Use daemon thread
     */
    private final ExecutorService executor = Executors.newCachedThreadPool(
            new NamedThreadFactory("Forking-cluster-timer",4, true));


    @Override
    public Object invoke(String serviceKey, TrpcRequest request) {
        try {
            ForkingConnectionManager forkingConnectionManager = ConnectionManagerFactory.getForkingConnectionManager();
            List<TrpcClientHandler> clientHandlers = forkingConnectionManager.handlerList(serviceKey);
            final BlockingQueue<Object> ref = new LinkedBlockingQueue<>();
            final AtomicInteger count = new AtomicInteger();
            for (TrpcClientHandler clientHandler : clientHandlers) {
                executor.execute(()->{
                    try {
                        TrpcFuture trpcFuture = clientHandler.sendRequest(request);
                        Object o = trpcFuture.get();
                        ref.offer(o);
                    }catch (Throwable e){
                        int value = count.incrementAndGet();
                        if (value>=clientHandlers.size()){
                            ref.offer(e);
                        }
                    }
                });
            }
            try {
                Object value = ref.poll(3, TimeUnit.SECONDS);
                if (value instanceof Throwable){
                    Throwable e = (Throwable) value;
                    LOGGER.error("Failed to forking invoke provider, but no luck to perform the invocation. Last error is: " + e.getMessage(), e.getCause() != null ? e.getCause() : e);
                }
                return value;
            }catch (Exception e){
                LOGGER.error("Failed to forking invoke provider, but no luck to perform the invocation. Last error is: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            LOGGER.error("ForkingInvoker exception",e);
        }
        return null;
    }
}
