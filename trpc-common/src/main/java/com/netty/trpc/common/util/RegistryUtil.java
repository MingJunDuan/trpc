package com.netty.trpc.common.util;

import com.netty.trpc.common.exception.CustomTrpcRuntimeException;

public class RegistryUtil {
    private static final String SPLIT="://";

    public static String protocol(String url){
        if (url == null || !url.contains(SPLIT)) {
            throw new CustomTrpcRuntimeException("Protocol must start with '"+SPLIT+"', "+url);
        }
        return url.substring(0, url.indexOf(SPLIT));
    }

    public static String registryAddress(String url){
        if (url == null || !url.contains(SPLIT)) {
            throw new CustomTrpcRuntimeException("Protocol must start with '"+SPLIT+"', "+url);
        }
        return url.substring(url.indexOf(SPLIT)+SPLIT.length());
    }
}
