package com.netty.trpc.common.util.timer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-12 09:03
 */
public class HashedWheelTimerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashedWheelTimerTest.class);

    @Test
    public void newTimeout() throws InterruptedException {
        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();
        hashedWheelTimer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                LOGGER.info("调用外部系统");
            }
        },4, TimeUnit.SECONDS);
        LOGGER.info("start");
        hashedWheelTimer.start();
        LOGGER.info("start end");

        TimeUnit.SECONDS.sleep(2);
        hashedWheelTimer.stop();
        LOGGER.info("stopped");
        TimeUnit.SECONDS.sleep(4);
    }
}