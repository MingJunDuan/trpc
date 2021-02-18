package com.netty.trpc.client.connect;

import com.netty.trpc.protocol.RpcProtocol;
import com.netty.trpc.util.threadpool.CallerRejectedExecutionHandler;
import com.netty.trpc.util.threadpool.EagerThreadPoolExecutor;
import com.netty.trpc.util.threadpool.NamedThreadFactory;
import com.netty.trpc.util.threadpool.TaskQueue;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 11:20
 */
public class ConnectionManager {
    private EventLoopGroup eventLoopGroup=new NioEventLoopGroup(4);
    private static EagerThreadPoolExecutor threadPoolExecutor = new EagerThreadPoolExecutor(4,8,600L, TimeUnit.SECONDS,
            new TaskQueue<>(1000),new NamedThreadFactory("connectionManager"), new CallerRejectedExecutionHandler());

    public static ConnectionManager getInstance() {
        return null;
    }

    public void removeHandler(RpcProtocol rpcProtocol) {

    }

}
