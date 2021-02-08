package com.netty.trpc.constant;

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
}
