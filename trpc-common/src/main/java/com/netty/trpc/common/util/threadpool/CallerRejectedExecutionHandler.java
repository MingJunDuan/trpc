package com.netty.trpc.common.util.threadpool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:34
 */
public class CallerRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            r.run();
        }
    }
}
