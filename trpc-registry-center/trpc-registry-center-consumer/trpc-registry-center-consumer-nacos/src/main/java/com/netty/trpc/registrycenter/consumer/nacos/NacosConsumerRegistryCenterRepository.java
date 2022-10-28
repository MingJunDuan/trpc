/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.registrycenter.consumer.nacos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netty.trpc.common.extension.SPI;
import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;
import com.netty.trpc.registrycenter.consumer.api.ConsumerRegistryCenterRepository;
import com.netty.trpc.registrycenter.consumer.api.ServiceEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmj1161859184@126.com 2022-09-28 07:31
 * @version 1.0
 * @since 1.0
 */
@SPI(name = "nacos")
public class NacosConsumerRegistryCenterRepository implements ConsumerRegistryCenterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosConsumerRegistryCenterRepository.class);
    private ServiceEventListener serviceEventListener;
    private NamingService namingService;

    @Override
    public void init(RegistryCenterMetadata metadata, ServiceEventListener eventListener) {
        this.serviceEventListener = eventListener;
        String nacosServerList = metadata.getServerList();
        try {
            namingService = NamingFactory.createNamingService(nacosServerList);
        } catch (NacosException e) {
            LOGGER.error("Init nacos naming server exception", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void subscribe(RegistryMetadata metadata) {
        List<RpcServiceMetaInfo> serviceInfoList = metadata.getServiceInfoList();
        for (RpcServiceMetaInfo rpcServiceMetaInfo : serviceInfoList) {
            String serviceName = rpcServiceMetaInfo.getServiceName()+":"+rpcServiceMetaInfo.getVersion();
            try {
                namingService.subscribe(serviceName, event -> {
                    if (event instanceof NamingEvent) {
                        List<Instance> instances = ((NamingEvent) event).getInstances();
                        List<RegistryMetadata> registryMetadatas=new LinkedList<>();
                        for (Instance instance : instances) {
                            LOGGER.info("{}", instance);
                            RegistryMetadata registryMetadata = getRegistryMetadata(instance);
                            registryMetadatas.add(registryMetadata);
                        }
                        serviceEventListener.publish(registryMetadatas);
                        LOGGER.info(instances.size()+"");
                    }
                });
            } catch (NacosException e) {
                LOGGER.error("Subscribe service exception", e);
                throw new RuntimeException(e);
            }
        }
    }

    private RegistryMetadata getRegistryMetadata(Instance instance) {
        String ip = instance.getIp();
        int port = instance.getPort();
        RegistryMetadata registryMetadata = new RegistryMetadata();
        registryMetadata.setHost(ip);
        registryMetadata.setPort(port);
        String tmpServiceName = instance.getServiceName();
        String tmpStr = tmpServiceName.substring(tmpServiceName.indexOf("@@")+2);
        tmpServiceName = tmpStr.substring(0, tmpStr.indexOf(":"));
        String version = tmpStr.substring(tmpStr.indexOf(":") + 1);
        RpcServiceMetaInfo rpcServiceMetaInfo1 = new RpcServiceMetaInfo();
        rpcServiceMetaInfo1.setServiceName(tmpServiceName);
        rpcServiceMetaInfo1.setVersion(version);
        registryMetadata.setServiceInfoList(Arrays.asList(rpcServiceMetaInfo1));
        return registryMetadata;
    }

    @Override
    public void unsubscribe(RegistryMetadata metadata) {
        List<RpcServiceMetaInfo> serviceInfoList = metadata.getServiceInfoList();
        for (RpcServiceMetaInfo rpcServiceMetaInfo : serviceInfoList) {
            String serviceName = rpcServiceMetaInfo.getServiceName();
            try {
                namingService.unsubscribe(serviceName, event -> {
                    if (event instanceof NamingEvent) {
                        List<Instance> instances = ((NamingEvent) event).getInstances();
                        List<RegistryMetadata> metadataArrayList = new ArrayList<>(instances.size());
                        for (Instance instance : instances) {
                            LOGGER.info("{}", instance);

                        }
                        LOGGER.info(instances.size()+"");
                    }
                });
            } catch (NacosException e) {
                LOGGER.error("Subscribe service exception", e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void destroy() {
        try {
            namingService.shutDown();
        } catch (NacosException e) {
            LOGGER.error("Shutdown namingService", e);
        }
    }
}
