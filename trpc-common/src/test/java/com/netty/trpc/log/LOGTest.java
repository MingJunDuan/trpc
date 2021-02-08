package com.netty.trpc.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 9:23
 */
public class LOGTest {
    private static final Logger log = LoggerFactory.getLogger(LOGTest.class);

    @Test
    public void test(){
        LOG.info("Info test");
        log.info("Info test");
    }
}
