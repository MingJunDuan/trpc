package com.netty.trpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 15:29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface TrpcService {

    Class value();

    String version() default "1.0";
}
