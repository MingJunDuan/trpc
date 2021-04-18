package com.netty.trpc.client.faulttolerance;

import com.netty.trpc.common.codec.TrpcRequest;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-01 14:55
 */
public class BroadcastInvoker implements Invoker{

    @Override
    public Object invoke(String serviceKey, TrpcRequest request) {
        return null;
    }
}
