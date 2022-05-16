/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.common.spi;

import java.util.Iterator;

/**
 * @author dmj1161859184@126.com 2022-05-16 23:03
 * @version 1.0
 * @since 1.0
 */
public class ServiceLoaderUtil {

    public static <T> T load(Class<T> clazz) {
        return load(clazz,Thread.currentThread().getContextClassLoader());
    }

    public static <T> T load(Class<T> clazz, ClassLoader classLoader) {
        java.util.ServiceLoader<T> serviceLoader = java.util.ServiceLoader.load(clazz, classLoader);
        Iterator<T> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
}
