/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.registrycenter.consumer.zookeeper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.netty.trpc.common.constant.TrpcConstant;
import com.netty.trpc.common.zookeeper.CuratorClient;
import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;
import com.netty.trpc.registrycenter.consumer.api.ConsumerRegistryCenterRepository;
import com.netty.trpc.registrycenter.consumer.api.ServiceEventListener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmj1161859184@126.com 2021-12-02 23:38
 * @version 1.0
 * @since 1.0
 */
public class ZookeeperConsumerRegistryCenterRepository implements ConsumerRegistryCenterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperConsumerRegistryCenterRepository.class);
    private ServiceEventListener serviceEventListener;
    private CuratorClient client;

    @Override
    public void init(RegistryCenterMetadata metadata, ServiceEventListener eventListener) {
        this.client = new CuratorClient(metadata.getServerList());
        this.serviceEventListener = eventListener;
    }

    @Override
    public void subscribe() {
//        try {
//            //Add watch listener
//            client.watchPathChildrenNode(TrpcConstant.ZK_DATA_PATH, new PathChildrenCacheListener() {
//                @Override
//                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
//                    PathChildrenCacheEvent.Type type = pathChildrenCacheEvent.getType();
//                    switch (type) {
//                        case CONNECTION_RECONNECTED:
//                            LOGGER.info("Reconnected to zk, try to get latest service list");
//                        case CHILD_ADDED:
//                        case CHILD_UPDATED:
//                        case CHILD_REMOVED:
//                            getRegistryMetadataAndPublishToListener(providerNode, serviceName);
//                            break;
//                    }
//                }
//            });
//            getRegistryMetadataAndPublishToListener(providerNode, serviceName);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//        }
    }

    @Override
    public void subscribe(RegistryMetadata metadata) {
        List<RpcServiceMetaInfo> serviceInfoList = metadata.getServiceInfoList();
        for (RpcServiceMetaInfo rpcServiceMetaInfo : serviceInfoList) {
            String serviceName = rpcServiceMetaInfo.getServiceName() + ":" + rpcServiceMetaInfo.getVersion();
            String providerNode = TrpcConstant.ZK_DATA_PATH + "/" + serviceName + "/" + TrpcConstant.ZK_DATA_PROVIDER;
            String consumerNode = TrpcConstant.ZK_DATA_PATH + "/" + serviceName + "/" + TrpcConstant.ZK_DATA_CONSUMER;
            String node = metadata.getHost() + ":" + metadata.getPort();

            registryConsumer(consumerNode, node);
            try {
                client.watchPathChildrenNode(providerNode, new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                        PathChildrenCacheEvent.Type type = pathChildrenCacheEvent.getType();
                        switch (type) {
                            case CONNECTION_RECONNECTED:
                                LOGGER.info("Reconnected to zk, try to get latest service list");
                            case CHILD_ADDED:
                            case CHILD_UPDATED:
                            case CHILD_REMOVED:
                                getRegistryMetadataAndPublishToListener(providerNode, serviceName);
                                break;
                        }
                    }
                });
                getRegistryMetadataAndPublishToListener(providerNode, serviceName);
            } catch (Exception e) {
                LOGGER.error("watch zookeeper service provider node error", e);
            }
        }

    }

    private void getRegistryMetadataAndPublishToListener(String providerNode, String serviceName) {
        try {
            List<String> nodeList = client.getChildren(providerNode);
            String[] items = serviceName.split(":");
            String svcName = items[0];
            String version = null;
            if (items.length > 1) {
                version = items[1];
            }
            for (String node : nodeList) {
                String[] list = node.split(":");
                String providerHost = list[0];
                String providerPort = list[1];

                RegistryMetadata registryMetadata = new RegistryMetadata();
                registryMetadata.setHost(providerHost);
                registryMetadata.setPort(Integer.parseInt(providerPort));

                RpcServiceMetaInfo rpcServiceMetaInfo = new RpcServiceMetaInfo();
                rpcServiceMetaInfo.setServiceName(svcName);
                rpcServiceMetaInfo.setVersion(version);

                registryMetadata.setServiceInfoList(Arrays.asList(rpcServiceMetaInfo));
                serviceEventListener.publish(Arrays.asList(registryMetadata));
                LOGGER.info("Service node data: {}", registryMetadata);
            }
        } catch (Exception e) {
            LOGGER.error("error in get registry metadata", e);
        }
    }

    private void registryConsumer(String consumerNode, String node) {
        try {
            if (!client.pathExist(consumerNode)) {
                client.createPathData(consumerNode, null, CreateMode.PERSISTENT);
            }
            client.createPathData(consumerNode + "/" + node, null);
        } catch (Exception e) {
            LOGGER.error("registry consumer in zookeeper error", e);
        }
    }

    @Override
    public void destroy() {
        client.close();
    }

    private void getRegistryMetadataAndPublishToListener() throws Exception {
        List<String> nodeList = client.getChildren(TrpcConstant.ZK_DATA_PATH);
        List<RegistryMetadata> metadataArrayList = new ArrayList<>(nodeList.size());
        for (String node : nodeList) {
            byte[] data = client.getData(TrpcConstant.ZK_DATA_PATH + "/" + node);
            String str = new String(data, StandardCharsets.UTF_8);
            RegistryMetadata registryMetadata = JSONObject.parseObject(str, RegistryMetadata.class);
            metadataArrayList.add(registryMetadata);
        }
        serviceEventListener.publish(metadataArrayList);
        LOGGER.info("Service node data: {}", metadataArrayList);
    }
}
