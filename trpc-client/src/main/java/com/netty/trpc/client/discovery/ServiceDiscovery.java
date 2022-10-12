package com.netty.trpc.client.discovery;

import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.consumer.api.ConsumerRegistryCenterRepository;
import com.netty.trpc.registrycenter.consumer.api.ServiceEventListener;
import com.netty.trpc.registrycenter.consumer.nacos.NacosConsumerRegistryCenterRepository;
import com.netty.trpc.registrycenter.consumer.zookeeper.ZookeeperConsumerRegistryCenterRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 10:31
 */
public class ServiceDiscovery {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);
    private ConsumerRegistryCenterRepository registryCenterRepository;
    private ServiceEventListener serviceEventListener = new ServiceEventClientListener();

    public ServiceDiscovery(String registryAddress) {
        registryCenterRepository = new NacosConsumerRegistryCenterRepository();
        RegistryCenterMetadata registryCenterMetadata = new RegistryCenterMetadata();
        registryCenterMetadata.setServerList(registryAddress);
        registryCenterRepository.init(registryCenterMetadata,serviceEventListener);
        registryCenterRepository.subscribe();
    }

    public void stop() {
        //
    }
}
