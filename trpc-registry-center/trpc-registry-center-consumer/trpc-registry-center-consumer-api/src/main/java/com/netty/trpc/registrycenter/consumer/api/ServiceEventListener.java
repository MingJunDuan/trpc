package com.netty.trpc.registrycenter.consumer.api;

/**
 * @author mjduan
 * @version 1.0
 * @date 2021-12-03 09:30
 */
public interface ServiceEventListener {

    <T> void publish(T event);
}
