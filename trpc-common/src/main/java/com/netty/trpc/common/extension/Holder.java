package com.netty.trpc.common.extension;

/**
 * Helper Class for hold a value.
 */
public class Holder<T> {

    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

}
