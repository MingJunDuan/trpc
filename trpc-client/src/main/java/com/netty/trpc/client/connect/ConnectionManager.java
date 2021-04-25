package com.netty.trpc.client.connect;

import com.alibaba.fastjson.JSONObject;
import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.handler.TrpcClientInitializer;
import com.netty.trpc.client.route.TrpcLoadBalance;
import com.netty.trpc.client.route.impl.TrpcLoadBalanceRoundRobin;
import com.netty.trpc.common.protocol.RpcProtocol;
import com.netty.trpc.common.protocol.RpcServiceInfo;
import com.netty.trpc.common.util.threadpool.CallerRejectedExecutionHandler;
import com.netty.trpc.common.util.threadpool.EagerThreadPoolExecutor;
import com.netty.trpc.common.util.threadpool.NamedThreadFactory;
import com.netty.trpc.common.util.threadpool.TaskQueue;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 11:20
 */
public class ConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
    private static EagerThreadPoolExecutor threadPoolExecutor = new EagerThreadPoolExecutor(4, 8, 600L, TimeUnit.SECONDS,
            new TaskQueue<>(1000), new NamedThreadFactory("ConnectionManager", 10), new CallerRejectedExecutionHandler());
    protected Map<RpcProtocol, TrpcClientHandler> connectedServerNodes = new ConcurrentHashMap<>();
    private CopyOnWriteArraySet<RpcProtocol> rpcProtocolSet = new CopyOnWriteArraySet<>();
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();
    protected TrpcLoadBalance loadBalance;
    private long waitTimeout = 5_000;
    protected volatile boolean isRunning = true;

    ConnectionManager() {
        loadBalance = new TrpcLoadBalanceRoundRobin();
    }

    public void removeHandler(RpcProtocol rpcProtocol) {
        rpcProtocolSet.remove(rpcProtocol);
        connectedServerNodes.remove(rpcProtocol);
        LOGGER.info("Remove one connection, host:{},port:{}", rpcProtocol.getHost(), rpcProtocol.getPort());
    }

    public TrpcClientHandler chooseHandler(String serviceKey) throws Exception {
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
        RpcProtocol rpcProtocol = loadBalance.route(serviceKey, connectedServerNodes);
        TrpcClientHandler trpcClientHandler = connectedServerNodes.get(rpcProtocol);
        if (trpcClientHandler != null) {
            return trpcClientHandler;
        } else {
            throw new Exception("Can not get available connection");
        }
    }

    protected boolean waitingForHandler() throws InterruptedException {
        lock.lock();
        try {
            LOGGER.warn("Waiting for available service");
            return connected.await(this.waitTimeout, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    public void updateConnectedServer(RpcProtocol rpcProtocol, PathChildrenCacheEvent.Type type) {
        if (rpcProtocol == null) {
            return;
        }
        if (type == PathChildrenCacheEvent.Type.CHILD_ADDED && !rpcProtocolSet.contains(rpcProtocol)) {
            connectServerNode(rpcProtocol);
        } else if (type == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
            //TODO 这里可以优化,如果IP和端口不变，那是不是不用重新进行connect操作
            removeAndCloseHandler(rpcProtocol);
            connectServerNode(rpcProtocol);
        } else if (type == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
            removeAndCloseHandler(rpcProtocol);
        } else {
            throw new IllegalArgumentException("Unknow type:" + type);
        }
    }

    public void updateConnectedServer(List<RpcProtocol> rpcProtocols) {
        if (rpcProtocols != null && rpcProtocols.size() > 0) {
            HashSet<RpcProtocol> tmpRpcProtocolSet = new HashSet<>(rpcProtocols.size());
            tmpRpcProtocolSet.addAll(rpcProtocols);

            for (RpcProtocol rpcProtocol : tmpRpcProtocolSet) {
                if (!rpcProtocolSet.contains(rpcProtocol)) {
                    connectServerNode(rpcProtocol);
                }
            }

            for (RpcProtocol rpcProtocol : rpcProtocolSet) {
                if (!tmpRpcProtocolSet.contains(rpcProtocol)) {
                    LOGGER.info("Remove invalid service:{}", JSONObject.toJSONString(rpcProtocol));
                    removeAndCloseHandler(rpcProtocol);
                }
            }
        } else {
            //No available service
            for (RpcProtocol rpcProtocol : rpcProtocolSet) {
                removeAndCloseHandler(rpcProtocol);
            }
        }
    }

    private void removeAndCloseHandler(RpcProtocol rpcProtocol) {
        TrpcClientHandler handler = connectedServerNodes.get(rpcProtocol);
        if (handler != null) {
            handler.close();
        }
        connectedServerNodes.remove(rpcProtocol);
        rpcProtocolSet.remove(rpcProtocol);
    }

    private void connectServerNode(RpcProtocol rpcProtocol) {
        if (CollectionUtils.isEmpty(rpcProtocol.getServiceInfoList())) {
            LOGGER.info("No service on node, host:{},port:{}", rpcProtocol.getHost(), rpcProtocol.getPort());
            return;
        }
        rpcProtocolSet.add(rpcProtocol);
        LOGGER.info("New server node, host:{},port:{}", rpcProtocol.getHost(), rpcProtocol.getPort());
        for (RpcServiceInfo rpcServiceInfo : rpcProtocol.getServiceInfoList()) {
            LOGGER.info("New service info, name:{},version:{}", rpcServiceInfo.getServiceName(), rpcServiceInfo.getVersion());
        }
        final InetSocketAddress remotePeer = new InetSocketAddress(rpcProtocol.getHost(), rpcProtocol.getPort());
        //用netty客户端建立与Server的连接
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventLoopGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new TrpcClientInitializer());
                ChannelFuture channelFuture = bootstrap.connect(remotePeer);
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (channelFuture.isSuccess()) {
                            LOGGER.info("Successfully connect to remote server, remote peer={}", remotePeer);
                            TrpcClientHandler trpcClientHandler = channelFuture.channel().pipeline().get(TrpcClientHandler.class);
                            connectedServerNodes.put(rpcProtocol, trpcClientHandler);
                            trpcClientHandler.setRpcProtocol(rpcProtocol);
                            signalAvailableHandler();
                        } else {
                            LOGGER.error("Can not connect to remote server, remote peer={}", remotePeer);
                        }
                    }
                });
            }
        });
    }

    private void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        isRunning = false;
        for (RpcProtocol rpcProtocol : rpcProtocolSet) {
            TrpcClientHandler handler = connectedServerNodes.get(rpcProtocol);
            if (handler != null) {
                handler.close();
            }
            connectedServerNodes.remove(rpcProtocol);
            rpcProtocolSet.remove(rpcProtocol);
        }
        signalAvailableHandler();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }
}
