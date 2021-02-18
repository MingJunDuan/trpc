package com.netty.trpc.client.route;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.protocol.RpcProtocol;

import java.util.Map;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 12:35
 */
public abstract class TrpcLoadBalance {
    public abstract RpcProtocol route(String serviceKey, Map<RpcProtocol, TrpcClientHandler> connectedServerNodes);
}
