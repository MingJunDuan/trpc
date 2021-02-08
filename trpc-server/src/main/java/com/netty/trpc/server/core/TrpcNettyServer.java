package com.netty.trpc.server.core;

import com.netty.trpc.log.LOG;
import com.netty.trpc.server.registry.ServiceRegistry;
import com.netty.trpc.util.ServiceUtil;
import com.netty.trpc.util.threadpool.CallerRejectedExecutionHandler;
import com.netty.trpc.util.threadpool.EagerThreadPoolExecutor;
import com.netty.trpc.util.threadpool.NamedThreadFactory;
import com.netty.trpc.util.threadpool.TaskQueue;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:27
 */
public class TrpcNettyServer extends TrpcAbstractServer {
    private Thread thread;
    private String serverAddress;
    private ServiceRegistry serviceRegistry;
    private Map<String, Object> serviceMap = new HashMap<>();

    public TrpcNettyServer(String serverAddress) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = new ServiceRegistry(serverAddress);
    }

    public void addService(String interfaceName, String version, Object serviceBean) {
        LOG.info("Add service,interface:{},version:{},bean:{}", interfaceName, version, serviceBean);
        String serviceKey = ServiceUtil.serviceKey(interfaceName, version);
        serviceMap.put(serviceKey, serviceBean);
    }

    @Override
    public void start() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(coreNum, coreNum * 2, 60, TimeUnit.SECONDS, new TaskQueue<>(1000),
                new NamedThreadFactory("server"), new CallerRejectedExecutionHandler());
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new TrpcServerInitializer(serviceMap, executor))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            String[] items = serverAddress.split(":");
            String host = items[0];
            int port = Integer.valueOf(items[1]);
            ChannelFuture future = bootstrap.bind(host, port).sync();
            serviceRegistry.registryService(host, port, serviceMap);
            LOG.info("Server started on port {}", port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error(e);
        } finally {
            try {
                serviceRegistry.unregistryService();
                workGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

    @Override
    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}
