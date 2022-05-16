/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.server.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import com.netty.trpc.common.util.threadpool.CallerRejectedExecutionHandler;
import com.netty.trpc.common.util.threadpool.EagerThreadPoolExecutor;
import com.netty.trpc.common.util.threadpool.NamedThreadFactory;
import com.netty.trpc.common.util.threadpool.TaskQueue;

/**
 * @author dmj1161859184@126.com 2022-05-16 23:11
 * @version 1.0
 * @since 1.0
 */
public class DefaultUserServiceExecutorImpl implements UserServiceExecutor {

    @Override
    public Executor getExecutor() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        TaskQueue<Runnable> workQueue = new TaskQueue<>(1000);
        EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(coreNum, coreNum * 2, 60, TimeUnit.SECONDS, workQueue,
                new NamedThreadFactory("serviceHandleThread"), new CallerRejectedExecutionHandler());
        workQueue.setExecutor(executor);
        return executor;
    }
}
