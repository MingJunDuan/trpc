package com.netty.trpc.config.api;

public interface TrpcConfigApi {

    default String getProperty(String key) {
        return (String) getProperty(key, null);
    }

    default Object getProperty(String key, Object defaultValue) {
        Object value = getInternalProperty(key);
        return value != null ? value : defaultValue;
    }

    Object getInternalProperty(String key);
}
