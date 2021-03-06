package com.netty.trpc.common.constant;

import com.netty.trpc.common.serializer.protostuff.ProtostuffIOSerializer;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 9:54
 */
public class TrpcConstant {
   public static final int ZK_SESSION_TIMEOUT = 5000;
   public static final int ZK_CONNECTION_TIMEOUT = 8000;

   public static final String ZK_REGISTRY_PATH = "/registry";
   public static final String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

   public static final String ZK_NAMESPACE = "netty-trpc";

   public static final int MAGIC_NUMBER=0xcafebabe;
   public static final short DEFAULT_SERIALIZER_ALGORITHM = ProtostuffIOSerializer.instance.type();
   public static final short TRPC_PROTOCOL_VERSION = 0x01;
}
