package com.netty.trpc.client.route.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.TrpcLoadBalance;
import com.netty.trpc.common.util.ServiceUtil;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-03-29 15:00
 */
public class TrpcRandomLoadBalanceImpl extends TrpcLoadBalance {
    private int defaultWeight = 100;

    @Override
    public RegistryMetadata route(String serviceKey, Map<RegistryMetadata, TrpcClientHandler> connectedServerNodes) throws Exception {
        Map<String, List<RegistryMetadata>> serviceMap = getServiceMap(connectedServerNodes);
        List<RegistryMetadata> addressList = serviceMap.get(serviceKey);

        return warmUp(serviceKey, addressList);
        //warm up
        //return addressList.get(new Random().nextInt(addressList.size()));
    }

    private RegistryMetadata warmUp(String serviceKey, List<RegistryMetadata> registryMetadatas) {
        int totalWeight = 0;
        boolean sameWeight = true;
        int[] weights = new int[registryMetadatas.size()];
        for (int i = 0; i < registryMetadatas.size(); i++) {
            RegistryMetadata registryMetadata = registryMetadatas.get(i);
            List<RpcServiceMetaInfo> serviceInfoList = registryMetadata.getServiceInfoList();
            for (int j = 0; j < serviceInfoList.size(); j++) {
                RpcServiceMetaInfo rpcServiceMetaInfo = serviceInfoList.get(j);
                String key = ServiceUtil.serviceKey(rpcServiceMetaInfo.getServiceName(), rpcServiceMetaInfo.getVersion());
                if (serviceKey.equals(key)) {
                    int weight = getWeight(rpcServiceMetaInfo);
                    totalWeight += weight;
                    weights[i] += totalWeight;
                    if (sameWeight && totalWeight != weight * (i + 1)) {
                        sameWeight = false;
                    }
                    break;
                }
            }
        }

        if (totalWeight > 0 && !sameWeight) {
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            for (int i = 0; i < registryMetadatas.size(); i++) {
                if (offset < weights[i]) {
                    return registryMetadatas.get(i);
                }
            }
        }


        return registryMetadatas.get(ThreadLocalRandom.current().nextInt(registryMetadatas.size()));
    }

    private int getWeight(RpcServiceMetaInfo rpcServiceMetaInfo) {
        if (rpcServiceMetaInfo.getWarmUp() == 0) {
            return 1;
        }

        long time = System.currentTimeMillis() - rpcServiceMetaInfo.getUptime();
        if (time < 0) {
            return 1;
        }
        if (time < rpcServiceMetaInfo.getWarmUp()) {
            //TODO 从rpc配置中读取，没有则取默认值
            int weight = defaultWeight;
            int value = (int) (time / (float) rpcServiceMetaInfo.getWarmUp() * weight);
            return value < 1 ? 1 : Math.min(value, weight);
        }
        return 1;
    }
}
