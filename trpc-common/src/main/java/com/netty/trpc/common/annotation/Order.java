package com.netty.trpc.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 15:48
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {

    int order() default 0;
}
