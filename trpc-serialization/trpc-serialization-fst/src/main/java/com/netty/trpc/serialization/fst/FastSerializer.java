package com.netty.trpc.serialization.fst;

import com.netty.trpc.serialization.api.Serializer;
import com.netty.trpc.serialization.api.SerializerType;
import org.nustaq.serialization.FSTConfiguration;

public class FastSerializer implements Serializer {
    private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    @Override
    public short type() {
        return SerializerType.FAST.getValue();
    }

    @Override
    public <T> byte[] serialize(T obj) {
        byte[] barray = conf.asByteArray(obj);
        return barray;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        T result = (T) conf.asObject(bytes);
        return result;
    }
}
