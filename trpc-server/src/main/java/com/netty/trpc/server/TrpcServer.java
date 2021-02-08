package com.netty.trpc.server;

import com.netty.trpc.server.core.TrpcNettyServer;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:01
 */
public class TrpcServer extends TrpcNettyServer {

    public TrpcServer(String serverAddress) {
        super(serverAddress);
    }

}
