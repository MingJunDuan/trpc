package com.netty.trpc.client.route.impl;

import java.util.List;
import java.util.Map;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.TrpcLoadBalance;
import com.netty.trpc.registrycenter.common.RegistryMetadata;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-15 22:34
 */
public class TrpcForkingLoadBalance extends TrpcLoadBalance {

    @Override
    public RegistryMetadata route(String serviceKey, Map<RegistryMetadata, TrpcClientHandler> connectedServerNodes) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RegistryMetadata> routes(String serviceKey, Map<RegistryMetadata, TrpcClientHandler> connectedServerNodes) throws Exception {
        Map<String, List<RegistryMetadata>> serviceMap = getServiceMap(connectedServerNodes);
        return serviceMap.get(serviceKey);
    }
}
