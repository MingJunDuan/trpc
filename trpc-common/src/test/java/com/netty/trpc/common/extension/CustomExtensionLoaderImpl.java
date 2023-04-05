package com.netty.trpc.common.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI(name = "customExtensionLoaderImpl")
public class CustomExtensionLoaderImpl implements CustomExtensionLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExtensionLoaderImpl.class);

    @Override
    public String sayHello() {
        String value = "Say hello from " + this.getClass().getCanonicalName();
        LOGGER.info(value);
        return value;
    }
}
