/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.common.util.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmj1161859184@126.com 2023-04-05 18:02
 * @version 1.0
 * @since 1.0
 */
public class ThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.error("Uncaughted exception threadName:{}", t.getName(), e);
    }
}
