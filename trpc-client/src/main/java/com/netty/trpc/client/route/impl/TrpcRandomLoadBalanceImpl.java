package com.netty.trpc.client.route.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.TrpcLoadBalance;
import com.netty.trpc.registrycenter.common.RegistryMetadata;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-03-29 15:00
 */
public class TrpcRandomLoadBalanceImpl extends TrpcLoadBalance {
    @Override
    public RegistryMetadata route(String serviceKey, Map<RegistryMetadata, TrpcClientHandler> connectedServerNodes) throws Exception {
        Map<String, List<RegistryMetadata>> serviceMap = getServiceMap(connectedServerNodes);
        List<RegistryMetadata> addressList = serviceMap.get(serviceKey);
        return addressList.get(new Random().nextInt(addressList.size()));
    }
}
