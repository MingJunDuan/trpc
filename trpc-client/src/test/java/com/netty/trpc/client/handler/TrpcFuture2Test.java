package com.netty.trpc.client.handler;

import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author dmj1161859184@126.com 2022-08-28 20:50
 * @version 1.0
 * @since 1.0
 */
public class TrpcFuture2Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcFuture2Test.class);

    @Test
    public void test_countDown() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        long count = countDownLatch.getCount();
        LOGGER.info("count:{}", count);

        countDownLatch.countDown();
        count = countDownLatch.getCount();
        LOGGER.info("count:{}", count);
        Assert.assertEquals(0, count);
    }

    @Test
    public void test() {

    }

}