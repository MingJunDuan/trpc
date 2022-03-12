package com.netty.trpc.common.serializer;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:02
 */
public interface Serializer {
   short type();

   <T> byte[] serialize(T obj);

   <T> T deserialize(byte[] bytes,Class<T> clazz);
}
