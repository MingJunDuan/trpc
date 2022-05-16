package com.netty.trpc.common.spi;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmj1161859184@126.com 2022-05-16 23:03
 * @version 1.0
 * @since 1.0
 */
public class ServiceLoaderUtilTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLoaderUtilTest.class);

    @Test
    public void load() {
        SPITestInterface spiTestInterface = ServiceLoaderUtil.load(SPITestInterface.class);
        String value = spiTestInterface.hello();
        LOGGER.info("value: '{}'", value);
    }
}