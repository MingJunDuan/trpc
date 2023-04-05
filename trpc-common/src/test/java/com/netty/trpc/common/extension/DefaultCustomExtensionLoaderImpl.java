package com.netty.trpc.common.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCustomExtensionLoaderImpl implements CustomExtensionLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCustomExtensionLoaderImpl.class);

    @Override
    public String sayHello() {
        String value = "Say hello from " + this.getClass().getCanonicalName();
        LOGGER.info(value);
        return value;
    }
}
