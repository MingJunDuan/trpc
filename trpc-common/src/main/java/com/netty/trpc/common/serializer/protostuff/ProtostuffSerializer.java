package com.netty.trpc.common.serializer.protostuff;

import com.netty.trpc.common.serializer.Serializer;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:11
 */
public class ProtostuffSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        throw new UnsupportedOperationException();
    }
}
