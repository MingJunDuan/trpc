package com.netty.trpc.common.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netty.trpc.common.extension.ExtensionLoader;
import com.netty.trpc.serialization.api.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.netty.trpc.common.constant.TrpcConstant.MAGIC_NUMBER;
import static com.netty.trpc.common.constant.TrpcConstant.TRPC_PROTOCOL_VERSION;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:15
 */
public class TrpcDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcDecoder.class);
    private static Map<Short, Serializer> serializerMap = new HashMap<>();
    private Class clazz;

    public TrpcDecoder(Class clazz) {
        this.clazz = clazz;
        initSerializerMap();
    }

    private void initSerializerMap() {
        ExtensionLoader<Serializer> extensionLoader = new ExtensionLoader(Serializer.class);
        List<Serializer> loadedExtensionInstances = extensionLoader.getLoadedExtensionInstances();
        for (Serializer serializer : loadedExtensionInstances) {
            serializerMap.put(serializer.type(),serializer);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 12) {
            return;
        }
        in.markReaderIndex();
        int magicNumber = in.readInt();
        if (MAGIC_NUMBER != magicNumber) {
            in.resetReaderIndex();
            return;
        }
        short version = in.readShort();
        assert version==TRPC_PROTOCOL_VERSION;

        short serializerAlgorithm = in.readShort();

        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        Serializer serializer = serializerMap.get(serializerAlgorithm);

        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object obj = null;
        try {
            obj = serializer.deserialize(data, clazz);
            out.add(obj);
        } catch (Exception e) {
            LOGGER.error("Decoder error", e);
        }
    }
}
