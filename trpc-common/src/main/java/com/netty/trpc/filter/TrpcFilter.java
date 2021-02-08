package com.netty.trpc.filter;

import com.netty.trpc.codec.TrpcRequest;
import com.netty.trpc.codec.TrpcResponse;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 15:46
 */
public interface TrpcFilter {

    default boolean preFilter(TrpcRequest request){
        return true;
    }

    TrpcResponse postFilter(TrpcRequest request, TrpcResponse response,Throwable throwable);
}
