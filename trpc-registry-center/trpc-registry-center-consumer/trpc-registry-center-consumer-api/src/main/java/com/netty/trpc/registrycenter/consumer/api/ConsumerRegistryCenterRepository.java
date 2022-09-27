package com.netty.trpc.registrycenter.consumer.api;

import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;

/**
 * @author mjduan
 * @version 1.0
 * @date 2021-12-01 12:23
 */
public interface ConsumerRegistryCenterRepository {

    void init(RegistryCenterMetadata metadata, ServiceEventListener eventListener);

    void subscribe();

    default void subscribe(RegistryMetadata metadata) {
    }

    default void unsubscribe(RegistryMetadata metadata) {
    }
}
