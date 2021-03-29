package com.netty.trpc.client.faulttolerance;

import com.netty.trpc.common.codec.TrpcRequest;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-25 23:01
 */
public interface Invoker {

    Object invoke(String serviceKey, TrpcRequest request);
}
