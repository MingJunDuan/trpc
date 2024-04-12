/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.server.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author dmj1161859184@126.com 2022-05-16 23:11
 * @version 1.0
 * @since 1.0
 */
public class VirtualThreadUserServiceExecutorImpl implements UserServiceExecutor {

    @Override
    public Executor getExecutor() {
        return Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual()
                        .name("serviceHandleThread", 1)
                        .factory());
    }
}
