/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.common.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmj1161859184@126.com 2022-05-16 23:04
 * @version 1.0
 * @since 1.0
 */
public class SPITestInterfaceImpl implements SPITestInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SPITestInterfaceImpl.class);

    @Override
    public String hello() {
        return "Hello from "+this.getClass().getSimpleName();
    }
}
