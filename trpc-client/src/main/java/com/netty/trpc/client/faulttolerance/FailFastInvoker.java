package com.netty.trpc.client.faulttolerance;

import com.netty.trpc.client.connect.ConnectionManager;
import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.handler.TrpcFuture;
import com.netty.trpc.common.codec.TrpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-25 23:07
 */
public class FailFastInvoker implements Invoker{
    private static final Logger LOGGER = LoggerFactory.getLogger(FailFastInvoker.class);

    @Override
    public Object invoke(String serviceKey, TrpcRequest request) {
        TrpcClientHandler handler = null;
        try {
            handler = ConnectionManager.getInstance().chooseHandler(serviceKey);
            TrpcFuture trpcFuture = handler.sendRequest(request);
            return trpcFuture.get();
        } catch (Exception e) {
            LOGGER.error("Call service fail", e);
            throw new RuntimeException("Call service fail for requestId:"+request.getRequestId());
        }
    }
}
