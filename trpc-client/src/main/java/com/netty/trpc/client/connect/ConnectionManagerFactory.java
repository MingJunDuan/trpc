package com.netty.trpc.client.connect;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-15 22:26
 */
public class ConnectionManagerFactory {
    private static final ConnectionManager instance = new ConnectionManager();
    private static final ForkingConnectionManager forkingInstance = new ForkingConnectionManager();

    public static ConnectionManager getConnectionManager(){
        return instance;
    }

    public static ForkingConnectionManager getForkingConnectionManager(){
        return forkingInstance;
    }
}
