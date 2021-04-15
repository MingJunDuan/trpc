package com.netty.trpc.client.connect;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.impl.TrpcForkingLoadBalance;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.common.protocol.RpcProtocol;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-15 22:25
 */
public class ForkingConnectionManager extends ConnectionManager {

    public ForkingConnectionManager() {
        loadBalance = new TrpcForkingLoadBalance();
    }

    public List<TrpcClientHandler> handlerList(String serviceKey) throws Exception {
        int size = connectedServerNodes.values().size();
        int count = 0, limit = 3;
        while (isRunning && size <= 0) {
            try {
                waitingForHandler();
                size = connectedServerNodes.values().size();
            } catch (InterruptedException e) {
                LOG.error("Waiting for avaiable service is interrupted!", e);
            }
            count++;
            if (count >= limit) {
                throw new Exception("Waiting for available service time out");
            }
        }
        List<RpcProtocol> rpcProtocols = loadBalance.routes(serviceKey, connectedServerNodes);
        List<TrpcClientHandler> clientHandlers = new ArrayList<>(rpcProtocols.size());
        for (RpcProtocol rpcProtocol : rpcProtocols) {
            clientHandlers.add(connectedServerNodes.get(rpcProtocol));
        }
        if (!clientHandlers.isEmpty()) {
            return clientHandlers;
        } else {
            throw new Exception("Can not get available connection");
        }
    }
}
