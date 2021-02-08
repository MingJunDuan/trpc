package com.netty.trpc.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LOG {
    private static final Logger LOGGER = LoggerFactory.getLogger(LOG.class);

    private LOG() {
    }

    public static void info(final Object arg) {
        if (arg == null) {
            return;
        }
        if (arg instanceof Throwable) {
            info(null, Throwable.class.cast(arg));
        } else {
            info(arg.toString(), null, null);
        }
    }

    public static void info(final String infoMessage, final Object... args) {
        LOGGER.info(infoMessage, args);
    }

    public static void info(final String infoMessage, final Throwable throwable) {
        LOGGER.info(infoMessage, throwable);
    }

    public static void warn(final String warnMessage) {
        warn(warnMessage, null, null);
    }

    public static void warn(final String infoMessage, final Object... args) {
        LOGGER.warn(infoMessage, args);
    }

    public static void warn(final String infoMessage, final Throwable throwable) {
        LOGGER.warn(infoMessage, throwable);
    }

    public static void error(final String errorMessage) {
        error(errorMessage, null, null);
    }

    public static void error(final String errorMessage, final Object... args) {
        LOGGER.error(errorMessage, args);
    }

    public static void error(final String errorMessage, final Throwable throwable) {
        LOGGER.error(errorMessage, throwable);
    }

    public static void error(final Throwable throwable) {
        error(null, throwable);
    }
}
