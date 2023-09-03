package com.netty.trpc.common.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netty.trpc.common.extension.ExtensionLoader;
import com.netty.trpc.serialization.api.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.netty.trpc.common.constant.TrpcConstant.DEFAULT_SERIALIZER_ALGORITHM;
import static com.netty.trpc.common.constant.TrpcConstant.MAGIC_NUMBER;
import static com.netty.trpc.common.constant.TrpcConstant.TRPC_PROTOCOL_VERSION;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:15
 */
public class TrpcEncoder extends MessageToByteEncoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcEncoder.class);
    private Class<?> clazz;
    private static Map<Short, Serializer> serializerMap = new HashMap<>();

    public TrpcEncoder(Class clazz){
        this.clazz = clazz;
        initSerializerMap();
    }

    private void initSerializerMap() {
        ExtensionLoader<Serializer> extensionLoader = ExtensionLoader.getExtensionLoader(Serializer.class);
        List<Serializer> loadedExtensionInstances = extensionLoader.getLoadedExtensionInstances();
        for (Serializer serializer : loadedExtensionInstances) {
            serializerMap.put(serializer.type(),serializer);
        }
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf out) throws Exception {
        if (clazz.isInstance(obj)) {
            try {
                Serializer serializer = serializerMap.get(DEFAULT_SERIALIZER_ALGORITHM);

                byte[] data = serializer.serialize(obj);
                out.writeInt(MAGIC_NUMBER);
                out.writeShort(TRPC_PROTOCOL_VERSION);
                out.writeShort(serializer.type());
                out.writeInt(data.length);
                out.writeBytes(data);
            }catch (Exception e){
                LOGGER.error("Encode error",e);
            }
        }
    }
}
