package com.netty.trpc.server.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.netty.trpc.common.filter.TrpcFilter;
import com.netty.trpc.common.util.ServiceUtil;
import com.netty.trpc.common.util.threadpool.CallerRejectedExecutionHandler;
import com.netty.trpc.common.util.threadpool.EagerThreadPoolExecutor;
import com.netty.trpc.common.util.threadpool.NamedThreadFactory;
import com.netty.trpc.common.util.threadpool.TaskQueue;
import com.netty.trpc.server.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:27
 */
public class TrpcNettyServer extends TrpcAbstractServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcNettyServer.class);
    private String serverAddress;
    private ServiceRegistry serviceRegistry;
    private Map<String, Object> serviceMap = new HashMap<>();
    private List<TrpcFilter> filters;

    public TrpcNettyServer(String serverAddress, String registryAddress) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = new ServiceRegistry(registryAddress);
    }

    public void addService(String interfaceName, String version, Object serviceBean) {
        LOGGER.info("Add service,interface:{},version:{},bean:{}", interfaceName, version, serviceBean);
        String serviceKey = ServiceUtil.serviceKey(interfaceName, version);
        serviceMap.put(serviceKey, serviceBean);
    }

    @Override
    public void start() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        TaskQueue<Runnable> workQueue = new TaskQueue<>(1000);
        EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(coreNum, coreNum * 2, 60, TimeUnit.SECONDS, workQueue,
                new NamedThreadFactory("server"), new CallerRejectedExecutionHandler());
        workQueue.setExecutor(executor);

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    //用于调整linux中accept queue的大小，在tcp三次连接时使用，参考：https://www.cnbLOGGERs.com/qiumingcheng/p/9492962.html
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //so_linger的值是-1、0、非0值
                    //-1表示socket.close()方法立即返回，但OS底层会将发送缓冲区全部发送到对端
                    //0表示socket.close方法立即返回，OS放弃发送缓冲区的数据直接向对端发送RST包，对端收到复位错误
                    //非0整数值表示调用socket.close()方法的线程被阻塞直到延迟时间到或发送缓冲区中的数据发送完毕，若超时，则对端会收到复位错误。
                    .option(ChannelOption.SO_LINGER, -1)
                    //Nagle算法，设置为true关闭Nagle算法，Nagle算法会将多个小的tcp包合并为大的包之后一块发送，所以开启Nagle算法会有延迟
                    .option(ChannelOption.TCP_NODELAY, true)
                    //复用time_wait的socket
                    .option(ChannelOption.SO_REUSEADDR, true)
                    //这是tcp层面的keepalive，不是应用层面的心跳，但是tcp层面的keepalive是有缺陷的，所以我们还需要应用层层面的心跳
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 1024 * 2, 1024 * 1024 * 8))
                    .childHandler(new TrpcServerInitializer(serviceMap, filters, executor));

            String[] items = serverAddress.split(":");
            String host = items[0];
            int port = Integer.valueOf(items[1]);
            ChannelFuture future = bootstrap.bind(host, port).sync();
            //启动完成之后再进行服务注册


            serviceRegistry.registryService(host, port, serviceMap);
            LOGGER.info("Server started on port {}", port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                serviceRegistry.unregistryService();
                workGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void setFilters(List<TrpcFilter> filters) {
        this.filters = filters;
    }

    @Override
    public void stop() {

    }
}
