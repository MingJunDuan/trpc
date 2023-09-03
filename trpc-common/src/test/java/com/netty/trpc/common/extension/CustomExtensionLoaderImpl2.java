package com.netty.trpc.common.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI(name = "customExtension2")
public class CustomExtensionLoaderImpl2 implements CustomExtensionLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExtensionLoaderImpl2.class);

    @Override
    public String sayHello() {
        String value = "Say hello from " + this.getClass().getCanonicalName();
        LOGGER.info(value);
        return value;
    }
}
