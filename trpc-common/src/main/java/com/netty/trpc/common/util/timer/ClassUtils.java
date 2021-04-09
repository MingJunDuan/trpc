package com.netty.trpc.common.util.timer;


/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-09 14:37
 */
public class ClassUtils {
    private static final char PACKAGE_SEPARATOR_CHAR = '.';

    public static String simpleClassName(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        String className = clazz.getName();
        final int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (lastDotIdx > -1) {
            return className.substring(lastDotIdx + 1);
        }
        return className;
    }
}
