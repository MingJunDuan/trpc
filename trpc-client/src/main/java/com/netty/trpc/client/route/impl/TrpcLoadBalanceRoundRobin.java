package com.netty.trpc.client.route.impl;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.TrpcLoadBalance;
import com.netty.trpc.common.protocol.RpcProtocol;

import java.util.Map;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 12:36
 */
public class TrpcLoadBalanceRoundRobin extends TrpcLoadBalance {

    @Override
    public RpcProtocol route(String serviceKey, Map<RpcProtocol, TrpcClientHandler> connectedServerNodes) {
        return null;
    }
}
