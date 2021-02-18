package com.netty.trpc.client.proxy;

import com.netty.trpc.client.handler.TrpcFuture;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 13:54
 */
public interface TrpcService<T,FN extends SerializableFunction<T>> {

    TrpcFuture call(String funcName,Object...args) throws Exception;

    //lambda method reference
    TrpcFuture call(FN fn,Object...args);
}
