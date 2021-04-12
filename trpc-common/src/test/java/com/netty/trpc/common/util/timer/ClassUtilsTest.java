package com.netty.trpc.common.util.timer;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-11 21:49
 */
public class ClassUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtilsTest.class);

    @Test
    public void simpleClassName() {
        String className = ClassUtils.simpleClassName(ClassUtils.class);
        LOGGER.info(className);
        Assert.assertEquals("ClassUtils",className);
    }
}