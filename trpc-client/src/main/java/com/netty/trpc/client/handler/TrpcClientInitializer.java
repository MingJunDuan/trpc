package com.netty.trpc.client.handler;

import java.util.concurrent.TimeUnit;

import com.netty.trpc.common.codec.PingPongRequest;
import com.netty.trpc.common.codec.TrpcDecoder;
import com.netty.trpc.common.codec.TrpcEncoder;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.serializer.protostuff.ProtostuffIOSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 13:04
 */
public class TrpcClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ProtostuffIOSerializer serializer = new ProtostuffIOSerializer();
        ChannelPipeline cp = ch.pipeline();
        //第一个参数是readIdle,第二个参数是writeIdle，第三个参数是allIdle，这些值设置为server端设置的一半
        cp.addLast(new IdleStateHandler(15, 15, PingPongRequest.BEAT_INTERVAL / 2, TimeUnit.SECONDS));
        cp.addLast(new TrpcEncoder(TrpcRequest.class, serializer));
        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        cp.addLast(new TrpcDecoder(TrpcResponse.class, serializer));
        cp.addLast(new TrpcClientHandler());
    }
}
