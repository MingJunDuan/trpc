package com.netty.trpc.common.zookeeper;

import java.util.List;

import com.netty.trpc.common.constant.TrpcConstant;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 9:50
 */
public class CuratorClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorClient.class);
    private CuratorFramework client;

    public CuratorClient(String connectString) {
        this(connectString, TrpcConstant.ZK_NAMESPACE);
    }

    public CuratorClient(String connectString, String namespace) {
        this(connectString, namespace, TrpcConstant.ZK_SESSION_TIMEOUT, TrpcConstant.ZK_CONNECTION_TIMEOUT);
    }

    /**
     * @param connectString     zk Ip and port
     * @param namespace         namespace
     * @param sessionTimeout    session
     * @param connectionTimeout connection
     */
    public CuratorClient(String connectString, String namespace, int sessionTimeout, int connectionTimeout) {
        client = CuratorFrameworkFactory.builder().namespace(namespace).connectString(connectString)
                .sessionTimeoutMs(sessionTimeout).connectionTimeoutMs(connectionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
        client.start();
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void addConnectionStateListener(ConnectionStateListener connectionStateListener) {
        client.getConnectionStateListenable().addListener(connectionStateListener);
    }

    public boolean pathExist(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            return stat != null;
        } catch (Exception e) {
            LOGGER.error("path exist error ", e);
        }
        return false;
    }

    public String createPathData(String path, byte[] data) throws Exception {
        return createPathData(path, data, CreateMode.EPHEMERAL);
    }

    public String createPathData(String path, byte[] data, CreateMode mode) throws Exception {
        return client.create().creatingParentsIfNeeded()
                .withMode(mode)
                .forPath(path, data);
    }

    public void updatePathData(String path, byte[] data) throws Exception {
        client.setData().forPath(path, data);
    }

    public void deletePath(String path) throws Exception {
        client.delete().forPath(path);
    }

    public String watchNode(String path, Watcher watcher) throws Exception {
        byte[] bytes = client.getData().usingWatcher(watcher).forPath(path);
        return new String(bytes);
    }

    public byte[] getData(String path) throws Exception {
        return client.getData().forPath(path);
    }


    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    public void watchTreeNode(String path, TreeCacheListener listener) {
        TreeCache treeCache = new TreeCache(client, path);
        treeCache.getListenable().addListener(listener);
    }

    public void watchPathChildrenNode(String path, PathChildrenCacheListener listener) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true);
        //BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        pathChildrenCache.getListenable().addListener(listener);
    }

    public void close() {
        client.close();
    }
}
