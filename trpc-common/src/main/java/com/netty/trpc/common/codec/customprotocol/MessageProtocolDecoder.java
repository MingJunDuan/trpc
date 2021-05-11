package com.netty.trpc.common.codec.customprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-05-11 17:00
 */
public class MessageProtocolDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        int type = in.readInt();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        TrpcMessageProtocol trpcMessageProtocol = new TrpcMessageProtocol(type,length,bytes);
        list.add(trpcMessageProtocol);
    }
}
