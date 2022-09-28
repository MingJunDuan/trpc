/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.registrycenter.consumer.zookeeper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.netty.trpc.common.constant.TrpcConstant;
import com.netty.trpc.common.zookeeper.CuratorClient;
import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.consumer.api.ConsumerRegistryCenterRepository;
import com.netty.trpc.registrycenter.consumer.api.ServiceEventListener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
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
    private CuratorClient curatorClient;

    @Override
    public void init(RegistryCenterMetadata metadata,ServiceEventListener eventListener) {
        this.curatorClient = new CuratorClient(metadata.getServerList());
        this.serviceEventListener = eventListener;
    }

    @Override
    public void subscribe() {
        try {
            //Add watch listener
            curatorClient.watchPathChildrenNode(TrpcConstant.ZK_DATA_PATH, new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    PathChildrenCacheEvent.Type type = pathChildrenCacheEvent.getType();
                    switch (type) {
                        case CONNECTION_RECONNECTED:
                            LOGGER.info("Reconnected to zk, try to get latest service list");
                        case CHILD_ADDED:
                        case CHILD_UPDATED:
                        case CHILD_REMOVED:
                            getRegistryMetadataAndPublishToListener();
                            break;
                    }
                }
            });
            getRegistryMetadataAndPublishToListener();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        curatorClient.close();
    }

    private void getRegistryMetadataAndPublishToListener() throws Exception {
        List<String> nodeList = curatorClient.getChildren(TrpcConstant.ZK_DATA_PATH);
        List<RegistryMetadata> metadataArrayList = new ArrayList<>(nodeList.size());
        for (String node : nodeList) {
            byte[] data = curatorClient.getData(TrpcConstant.ZK_DATA_PATH + "/" + node);
            String str = new String(data, StandardCharsets.UTF_8);
            RegistryMetadata registryMetadata = JSONObject.parseObject(str, RegistryMetadata.class);
            metadataArrayList.add(registryMetadata);
        }
        serviceEventListener.publish(metadataArrayList);
        LOGGER.info("Service node data: {}", metadataArrayList);
    }
}
