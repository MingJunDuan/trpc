package com.netty.trpc.common.serializer.dyuprotostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.netty.trpc.common.serializer.Serializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:11
 */
public class DyuProtostuffSerializer implements Serializer {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private <T> Schema<T> getSchema(Class<T> clazz) {
        return (Schema<T>) cachedSchema.computeIfAbsent(clazz, new Function<Class<?>, Schema<?>>() {
            @Override
            public Schema<?> apply(Class<?> aClass) {
                return RuntimeSchema.createFrom(clazz);
            }
        });
    }

    @Override
    public <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema<T> schema = getSchema(clazz);
        try {
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException("Protostuff serialize exception", e);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException("Protostuff deserialize exception", e);
        }
    }
}
