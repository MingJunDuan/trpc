package com.netty.trpc.server.core;

import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 16:30
 */
public class CustomChannelFutureListener implements ChannelFutureListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomChannelFutureListener.class);
    private TrpcRequest request;
    private TrpcResponse response;
    private Throwable throwable;

    public CustomChannelFutureListener(TrpcRequest request, TrpcResponse response, Throwable throwable) {
        this.request = request;
        this.response = response;
        this.throwable = throwable;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        LOGGER.info("Send response for request:{}", response.getRequestId());
    }
}
