package com.netty.trpc.common.extension;


import com.netty.trpc.common.annotation.Order;
import com.netty.trpc.common.lang.Prioritized;

public interface LoadingStrategy extends Prioritized {

    String directory();

    default boolean preferExtensionClassLoader() {
        return false;
    }

    default String[] excludedPackages() {
        return null;
    }

    default boolean overridden() {
        return false;
    }
}
