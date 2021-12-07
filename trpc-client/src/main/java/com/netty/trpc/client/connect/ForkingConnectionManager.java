package com.netty.trpc.client.connect;

import java.util.ArrayList;
import java.util.List;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.route.impl.TrpcForkingLoadBalance;
import com.netty.trpc.registrycenter.common.RegistryMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-15 22:25
 */
public class ForkingConnectionManager extends ConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForkingConnectionManager.class);
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
                LOGGER.error("Waiting for avaiable service is interrupted!", e);
            }
            count++;
            if (count >= limit) {
                throw new Exception("Waiting for available service time out");
            }
        }
        List<RegistryMetadata> rpcProtocols = loadBalance.routes(serviceKey, connectedServerNodes);
        List<TrpcClientHandler> clientHandlers = new ArrayList<>(rpcProtocols.size());
        for (RegistryMetadata rpcProtocol : rpcProtocols) {
            clientHandlers.add(connectedServerNodes.get(rpcProtocol));
        }
        if (!clientHandlers.isEmpty()) {
            return clientHandlers;
        } else {
            throw new Exception("Can not get available connection");
        }
    }
}
