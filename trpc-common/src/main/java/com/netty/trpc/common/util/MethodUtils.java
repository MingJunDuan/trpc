package com.netty.trpc.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-06-10 01:46
 */
public class MethodUtils {

    public static Object invokeMethod(Object serviceBean, Class<?> beanClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = beanClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }
}
