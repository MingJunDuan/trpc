package com.netty.trpc.common.codec;

import com.netty.trpc.common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:15
 */
public class TrpcEncoder extends MessageToByteEncoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcEncoder.class);
    private Class<?> clazz;
    private Serializer serializer;

    public TrpcEncoder(Class clazz,Serializer serializer){
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf out) throws Exception {
        if (clazz.isInstance(obj)) {
            try {
                byte[] data = serializer.serialize(obj);
                out.writeInt(data.length);
                out.writeBytes(data);
            }catch (Exception e){
                LOGGER.error("Encode error",e);
            }
        }
    }
}
