package com.netty.trpc.client.discovery;

import com.netty.trpc.common.extension.ExtensionLoader;
import com.netty.trpc.common.util.RegistryUtil;
import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;
import com.netty.trpc.registrycenter.consumer.api.ConsumerRegistryCenterRepository;
import com.netty.trpc.registrycenter.consumer.api.ServiceEventListener;
import com.netty.trpc.registrycenter.consumer.nacos.NacosConsumerRegistryCenterRepository;
import com.netty.trpc.registrycenter.consumer.zookeeper.ZookeeperConsumerRegistryCenterRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        ExtensionLoader<ConsumerRegistryCenterRepository> loaderExtensionLoader = new ExtensionLoader<>(ConsumerRegistryCenterRepository.class);
        registryCenterRepository =  loaderExtensionLoader.getExtension(RegistryUtil.protocol(registryAddress));
        RegistryCenterMetadata registryCenterMetadata = new RegistryCenterMetadata();
        registryCenterMetadata.setServerList(registryAddress);
        registryCenterRepository.init(registryCenterMetadata, serviceEventListener);
    }

    public void subscribe(Set<RpcServiceMetaInfo> rpcServiceMetaInfos) {
        if (rpcServiceMetaInfos.isEmpty()) {
            return;
        }
        List<RpcServiceMetaInfo> rpcServiceMetaInfoLIst = new LinkedList<>();
        for (RpcServiceMetaInfo rpcServiceMetaInfo : rpcServiceMetaInfos) {
            rpcServiceMetaInfoLIst.add(rpcServiceMetaInfo);
        }
        RegistryMetadata registryMetadata = new RegistryMetadata();
        registryMetadata.setServiceInfoList(rpcServiceMetaInfoLIst);
        registryCenterRepository.subscribe(registryMetadata);
    }

    public void stop() {
        //
    }
}
