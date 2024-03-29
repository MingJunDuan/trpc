package com.netty.trpc.common.util.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:54
 */
public class NamedThreadFactory implements ThreadFactory {
    protected static final AtomicInteger POOL_SEQ = new AtomicInteger(1);
    private static final Thread.UncaughtExceptionHandler expectionHandler = new ThreadUncaughtExceptionHandler();

    protected final AtomicInteger mThreadNum = new AtomicInteger(1);

    protected final String mPrefix;

    protected final boolean mDaemon;

    protected final ThreadGroup mGroup;
    //max priority is 10, it will get more cpu time
    protected final int priority;

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.getAndIncrement());
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, 5);
    }

    public NamedThreadFactory(String prefix, int priority) {
        this(prefix, priority, false);
    }

    public NamedThreadFactory(String prefix, int priority, boolean daemon) {
        mPrefix = prefix + "-thread-";
        mDaemon = daemon;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread thread = new Thread(mGroup, runnable, name, 0);
        thread.setDaemon(mDaemon);
        thread.setPriority(priority);
        thread.setUncaughtExceptionHandler(expectionHandler);
        return thread;
    }
}
