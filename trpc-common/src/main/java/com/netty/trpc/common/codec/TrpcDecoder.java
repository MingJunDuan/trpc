package com.netty.trpc.common.codec;

import com.netty.trpc.common.log.LOG;
import com.netty.trpc.common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:15
 */
public class TrpcDecoder extends ByteToMessageDecoder {
    private Class clazz;
    private Serializer serializer;

    public TrpcDecoder(Class clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object obj = null;
        try {
            obj = serializer.deserialize(data, clazz);
            out.add(obj);
        } catch (Exception e) {
            LOG.error("Decoder error", e);
        }

    }
}
