package com.netty.trpc.serialization.fury;

import com.netty.trpc.serialization.api.Serializer;
import com.netty.trpc.serialization.api.SerializerType;
import io.fury.Fury;
import io.fury.config.Language;

/**
 * Apache fury, reference: https://github.com/apache/incubator-fury
 */
public class FurySerializer implements Serializer {
    private Fury fury = Fury.builder().withLanguage(Language.JAVA)
            .requireClassRegistration(false)
            .build();

    @Override
    public short type() {
        return SerializerType.FURY.getValue();
    }

    @Override
    public <T> byte[] serialize(T obj) {
        byte[] bytes = fury.serialize(obj);
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        T object = (T) fury.deserialize(bytes);
        return object;
    }
}
