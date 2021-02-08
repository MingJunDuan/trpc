package com.netty.trpc.client.connect;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-04-15 22:26
 */
public class ConnectionManagerFactory {
    //volatile is necessary
    private static volatile ConnectionManager instance;
    private static volatile ForkingConnectionManager forkingInstance;

    /**
     * Lazy initialization
     *
     * @return
     */
    public static ConnectionManager getConnectionManager(){
        if (instance==null){
            synchronized (ConnectionManagerFactory.class){
                if (instance==null){
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    public static ForkingConnectionManager getForkingConnectionManager(){
        if (forkingInstance==null){
            synchronized (ConnectionManagerFactory.class){
                if (forkingInstance==null){
                    forkingInstance = new ForkingConnectionManager();
                }
            }
        }
        return forkingInstance;
    }
}
