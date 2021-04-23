package com.netty.trpc.common.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 9:23
 */
public class LOGTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LOGTest.class);

    @Test
    public void test(){
        LOGGER.info("Info test");
        LOGGER.info("Info test");
    }
}
