/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.common.serializer.protostuff;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.netty.trpc.common.serializer.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author dmj1161859184@126.com 2021-10-09 22:22
 * @version 1.0
 * @since 1.0
 */
public class ProtostuffIOSerializer implements Serializer {
    public static final ProtostuffIOSerializer instance=new ProtostuffIOSerializer();
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private <T> Schema<T> getSchema(Class<T> clazz) {
        return (Schema<T>) cachedSchema.computeIfAbsent(clazz, new Function<Class<?>, Schema<?>>() {
            @Override
            public Schema<?> apply(Class<?> aClass) {
                return RuntimeSchema.getSchema(clazz);
            }
        });
    }

    @Override
    public short type() {
        return 0;
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
