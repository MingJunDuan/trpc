package com.netty.trpc.client.route.impl;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.TrpcLoadBalance;
import com.netty.trpc.common.protocol.RpcProtocol;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-03-29 15:00
 */
public class TrpcRandomLoadBalanceImpl extends TrpcLoadBalance {
    @Override
    public RpcProtocol route(String serviceKey, Map<RpcProtocol, TrpcClientHandler> connectedServerNodes) throws Exception {
        Map<String, List<RpcProtocol>> serviceMap = getServiceMap(connectedServerNodes);
        List<RpcProtocol> addressList = serviceMap.get(serviceKey);
        return addressList.get(new Random().nextInt(addressList.size()));
    }
}
