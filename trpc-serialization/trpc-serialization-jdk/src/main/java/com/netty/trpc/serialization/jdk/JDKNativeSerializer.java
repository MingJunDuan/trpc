package com.netty.trpc.serialization.jdk;

import com.netty.trpc.common.extension.SPI;
import com.netty.trpc.serialization.api.Serializer;

@SPI(name = "jdkNative")
public class JDKNativeSerializer implements Serializer {
    @Override
    public short type() {
        return 0;
    }

    @Override
    public <T> byte[] serialize(T t) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> aClass) {
        return null;
    }
}
