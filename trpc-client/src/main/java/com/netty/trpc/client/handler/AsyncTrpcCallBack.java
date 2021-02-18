package com.netty.trpc.client.handler;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 11:38
 */
public interface AsyncTrpcCallBack {

    void success(Object result);

    void fail(Exception e);
}
