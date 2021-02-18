package com.netty.trpc.client.route.impl;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.TrpcLoadBalance;
import com.netty.trpc.common.protocol.RpcProtocol;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 12:36
 */
public class TrpcLoadBalanceRoundRobin extends TrpcLoadBalance {
    private AtomicInteger roundRobin = new AtomicInteger(0);

    @Override
    public RpcProtocol route(String serviceKey, Map<RpcProtocol, TrpcClientHandler> connectedServerNodes) throws Exception {
        Map<String, List<RpcProtocol>> serviceMap = getServiceMap(connectedServerNodes);
        List<RpcProtocol> addressList = serviceMap.get(serviceKey);
        if (addressList!=null&&addressList.size()>0){
            return doRoute(addressList);
        }else {
            throw new Exception("Can not find connection for service:"+serviceKey);
        }
    }

    private RpcProtocol doRoute(List<RpcProtocol> addressList) {
        int size = addressList.size();
        int index = roundRobin.getAndAdd(1) % size;
        return addressList.get(index);
    }
}
