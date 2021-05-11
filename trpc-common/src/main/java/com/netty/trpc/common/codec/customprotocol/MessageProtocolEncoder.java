package com.netty.trpc.common.codec.customprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-05-11 17:05
 */
public class MessageProtocolEncoder extends MessageToByteEncoder<TrpcMessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TrpcMessageProtocol trpcMessageProtocol, ByteBuf out) throws Exception {
        out.writeInt(trpcMessageProtocol.getType());
        out.writeInt(trpcMessageProtocol.getLength());
        out.writeBytes(trpcMessageProtocol.getContent());
    }
}
