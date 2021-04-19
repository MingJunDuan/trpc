package com.netty.trpc.client.faulttolerance;

import com.netty.trpc.client.connect.ConnectionManagerFactory;
import com.netty.trpc.client.connect.ForkingConnectionManager;
import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.handler.TrpcFuture;
import com.netty.trpc.common.codec.TrpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-01 14:55
 */
public class BroadcastInvoker implements Invoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastInvoker.class);
    private static final float broadcastFailPercent = 50 / 100;

    @Override
    public Object invoke(String serviceKey, TrpcRequest request) {
        try {
            ForkingConnectionManager forkingConnectionManager = ConnectionManagerFactory.getForkingConnectionManager();
            List<TrpcClientHandler> clientHandlers = forkingConnectionManager.handlerList(serviceKey);
            Object result = null;
            int failCount = 0;
            int failThresholdIndex = (int) (clientHandlers.size() * broadcastFailPercent);
            Throwable throwable = null;
            for (TrpcClientHandler clientHandler : clientHandlers) {
                try {
                    TrpcFuture trpcFuture = clientHandler.sendRequest(request);
                    result = trpcFuture.get();
                } catch (Throwable e) {
                    LOGGER.warn(e.getMessage(), e);
                    if (failCount++ >= failThresholdIndex) {
                        throwable = e;
                        break;
                    }
                }
            }
            if (failCount >= failThresholdIndex) {
                LOGGER.error(throwable.getMessage(), throwable);
                return null;
            }
            return result;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return null;
        }
    }
}
