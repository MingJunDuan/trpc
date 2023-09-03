package com.netty.trpc.client.discovery;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.netty.trpc.common.extension.ExtensionLoader;
import com.netty.trpc.common.util.RegistryUtil;
import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;
import com.netty.trpc.registrycenter.consumer.api.ConsumerRegistryCenterRepository;
import com.netty.trpc.registrycenter.consumer.api.ServiceEventListener;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 10:31
 */
public class ServiceDiscovery {
    private ConsumerRegistryCenterRepository registryCenterRepository;
    private ServiceEventListener serviceEventListener = new ServiceEventClientListener();

    public ServiceDiscovery(String registryAddress) {
        ExtensionLoader<ConsumerRegistryCenterRepository> loaderExtensionLoader = ExtensionLoader.getExtensionLoader(ConsumerRegistryCenterRepository.class);
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
        rpcServiceMetaInfoLIst.addAll(rpcServiceMetaInfos);
        RegistryMetadata registryMetadata = new RegistryMetadata();
        registryMetadata.setServiceInfoList(rpcServiceMetaInfoLIst);
        registryCenterRepository.subscribe(registryMetadata);
    }

    public void stop() {
        //
    }
}
