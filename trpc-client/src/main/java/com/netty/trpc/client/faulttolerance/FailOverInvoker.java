package com.netty.trpc.client.faulttolerance;

import com.netty.trpc.client.connect.ConnectionManager;
import com.netty.trpc.client.connect.ConnectionManagerFactory;
import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.handler.TrpcFuture;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-03-02 9:16
 */
public class FailOverInvoker implements Invoker{
    private static final Logger LOGGER = LoggerFactory.getLogger(FailOverInvoker.class);
    private static final int retryCount = 1;

    @Override
    public Object invoke(String serviceKey, TrpcRequest request) {
        TrpcClientHandler handler = null;
        TrpcResponse response=null;
        int count=0;
        do{
            try {
                handler = ConnectionManagerFactory.getConnectionManager().chooseHandler(serviceKey);
                response = doRequest(handler,request);
            } catch (Exception e) {
                LOGGER.error("Call service fail",e);
            }
            if (response!=null && !response.isError()){
                break;
            }
        }while (count++ < retryCount);
        if (response.isError()){
            LOGGER.error("Call service error after retry {} times, {}",retryCount,response.getError());
        }
        return response.getResult();
    }

    private TrpcResponse doRequest(TrpcClientHandler handler, TrpcRequest request) throws ExecutionException, InterruptedException {
        TrpcFuture trpcFuture = handler.sendRequest(request);
        return trpcFuture.getResponse();
    }
}
