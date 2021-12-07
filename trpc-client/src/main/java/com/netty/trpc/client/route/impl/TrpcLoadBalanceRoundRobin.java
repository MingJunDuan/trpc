package com.netty.trpc.client.route.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.TrpcLoadBalance;
import com.netty.trpc.registrycenter.common.RegistryMetadata;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 12:36
 */
public class TrpcLoadBalanceRoundRobin extends TrpcLoadBalance {
    private AtomicInteger roundRobin = new AtomicInteger(0);

    @Override
    public RegistryMetadata route(String serviceKey, Map<RegistryMetadata, TrpcClientHandler> connectedServerNodes) throws Exception {
        Map<String, List<RegistryMetadata>> serviceMap = getServiceMap(connectedServerNodes);
        List<RegistryMetadata> addressList = serviceMap.get(serviceKey);
        if (addressList!=null&&addressList.size()>0){
            return doRoute(addressList);
        }else {
            throw new Exception("Can not find connection for service:"+serviceKey);
        }
    }

    private RegistryMetadata doRoute(List<RegistryMetadata> addressList) {
        int size = addressList.size();
        int index = roundRobin.getAndAdd(1) % size;
        return addressList.get(index);
    }
}
