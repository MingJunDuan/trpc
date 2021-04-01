package com.netty.trpc.client.faulttolerance;

import com.netty.trpc.client.connect.ConnectionManager;
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
 * @date 2021-02-25 23:07
 */
public class FailSafeInvoker implements Invoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(FailRetryInvoker.class);

    @Override
    public Object invoke(String serviceKey, TrpcRequest request) {
        return null;
    }
}
