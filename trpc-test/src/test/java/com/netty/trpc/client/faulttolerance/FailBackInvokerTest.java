package com.netty.trpc.client.faulttolerance;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-15 22:09
 */
public class FailBackInvokerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FailBackInvokerTest.class);

    @Test
    public void invoke() {

    }

    @Test
    public void test_completeFuture() throws InterruptedException, ExecutionException {
        CompletableFuture<Object> future = new CompletableFuture<>();
        LOGGER.info("start");
        future.whenComplete(new BiConsumer<Object, Throwable>() {

            @Override
            public void accept(Object o, Throwable throwable) {
                LOGGER.info("whenComplete method");
                Assert.assertEquals(Integer.MIN_VALUE, o);
            }
        });
        TimeUnit.SECONDS.sleep(2);
        LOGGER.info("Before call complete");
        future.complete(Integer.MIN_VALUE);
        TimeUnit.SECONDS.sleep(1);
        Object o = future.get();
        Assert.assertEquals(Integer.MIN_VALUE, o);
    }
}