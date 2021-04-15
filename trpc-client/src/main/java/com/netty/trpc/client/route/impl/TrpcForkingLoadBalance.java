package com.netty.trpc.client.route.impl;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.TrpcLoadBalance;
import com.netty.trpc.common.protocol.RpcProtocol;

import java.util.List;
import java.util.Map;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-15 22:34
 */
public class TrpcForkingLoadBalance extends TrpcLoadBalance {

    @Override
    public RpcProtocol route(String serviceKey, Map<RpcProtocol, TrpcClientHandler> connectedServerNodes) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RpcProtocol> routes(String serviceKey, Map<RpcProtocol, TrpcClientHandler> connectedServerNodes) throws Exception {
        Map<String, List<RpcProtocol>> serviceMap = getServiceMap(connectedServerNodes);
        return serviceMap.get(serviceKey);
    }
}
