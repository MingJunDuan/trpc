package com.netty.trpc.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Optimize System.currentTimeMillis() issue in high concurrent, why System.currentTimeMillis() is slow, because it
 * call system kernel to get current time
 *
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-30 09:28
 */
public class SystemClock {
    private static final SystemClock INSTANCE = new SystemClock(1);
    private final int period;

    private final AtomicLong now;

    private SystemClock(int period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "SystemClock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    private long now() {
        return now.get();
    }

    /**
     * 用来替换原来的System.currentTimeMillis()
     */
    public static long currentTimeMillis() {
        return INSTANCE.now();
    }
}
