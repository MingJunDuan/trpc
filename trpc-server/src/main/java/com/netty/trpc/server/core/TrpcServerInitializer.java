package com.netty.trpc.server.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.netty.trpc.common.codec.PingPongRequest;
import com.netty.trpc.common.codec.TrpcDecoder;
import com.netty.trpc.common.codec.TrpcEncoder;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.filter.TrpcFilter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:37
 */
public class TrpcServerInitializer extends ChannelInitializer<SocketChannel> {
    private Map<String, Object> handlerMap;
    private List<TrpcFilter> filters;
    private ThreadPoolExecutor executor;

    public TrpcServerInitializer(Map<String, Object> handlerMap, List<TrpcFilter> filters, ThreadPoolExecutor executor) {
        this.handlerMap = handlerMap;
        this.filters = filters;
        this.executor = executor;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(30, 30, PingPongRequest.BEAT_INTERVAL, TimeUnit.SECONDS));
        pipeline.addLast(new TrpcDecoder(TrpcRequest.class));
        pipeline.addLast(new TrpcEncoder(TrpcResponse.class));
        pipeline.addLast(new TrpcServerHandler(handlerMap, filters, executor));
    }
}
