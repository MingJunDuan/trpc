package com.netty.trpc.client.handler;

import com.netty.trpc.common.codec.PingPongRequest;
import com.netty.trpc.common.codec.TrpcDecoder;
import com.netty.trpc.common.codec.TrpcEncoder;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.serializer.hessian.HessianSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 13:04
 */
public class TrpcClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        HessianSerializer serializer = new HessianSerializer();
        ChannelPipeline cp = ch.pipeline();
        cp.addLast(new IdleStateHandler(0,0, PingPongRequest.BEAT_INTERVAL, TimeUnit.SECONDS));
        cp.addLast(new TrpcEncoder(TrpcRequest.class,serializer));
        cp.addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0));
        cp.addLast(new TrpcDecoder(TrpcResponse.class,serializer));
        cp.addLast(new TrpcClientHandler());
    }
}
