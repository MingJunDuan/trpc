package com.netty.trpc.registrycenter.provider.api;

import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;

/**
 * @author mjduan
 * @version 1.0
 * @date 2021-12-01 12:23
 */
public interface ProviderRegistryCenterRepository {

    void init(RegistryCenterMetadata metadata);

    /**
     * registry
     *
     * @param metadata
     */
    void registry(RegistryMetadata metadata);

    /**
     * 取消注册
     */
    void unregistry(RegistryMetadata metadata);
}
