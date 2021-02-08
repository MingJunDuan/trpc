package com.netty.trpc.zookeeper;

import com.netty.trpc.BaseTest;
import com.netty.trpc.log.LOG;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 10:03
 */
public class CuratorClientTest extends BaseTest {
    private CuratorClient client;

    @Override
    protected void doBefore() {
        client = new CuratorClient(zkConnectStr, zkNamespaceStr);
    }

    @Override
    protected void doAfter() {
        client.close();
    }

    @Test
    public void test_connect() throws Exception {
        String path = "/client1";
        String data = "ok";
        String realPath = client.createPathData(path, data.getBytes(StandardCharsets.UTF_8));
        LOG.info(realPath);
        byte[] dataByte = client.getData(realPath);
        assertEquals(data, new String(dataByte));
        List<String> children = client.getChildren("/");
        assertEquals(1, children.size());
        children.forEach(p -> {
            LOG.info(p);
            deletePathQuite(p);
        });
        children = client.getChildren("/");
        assertEquals(0, children.size());
    }

    private void deletePathQuite(String path) {
        try {
            path = path.startsWith("/") ? path : "/" + path;
            client.deletePath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_watch() throws Exception {
        String path="/client";
        String data="value";
        String realPath = client.createPathData(path, data.getBytes());
        LOG.info(realPath);
        //只能监听某个节点的变化，不能监听子节点的变化
        client.watchNode("/", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                LOG.info(event);
            }
        });
        LOG.info("update data");
        client.updatePathData(realPath,"newValue".getBytes());
        LOG.info("create sub path");
        try {
            client.createPathData(realPath + "/subPath", "subPathValue".getBytes());
        }catch (Exception e){
            LOG.error(e);
        }
        LOG.info("create new path");
        String newRealPath = client.createPathData(path, "value2".getBytes());
        LOG.info(newRealPath);
        client.deletePath(realPath);
        client.deletePath(newRealPath);
        TimeUnit.SECONDS.sleep(4);
    }

    @Test
    public void test_watchTreeNode() throws Exception {
        client.watchTreeNode("/", new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                LOG.info("type:{},data:{},oldData:{}",treeCacheEvent.getType(),treeCacheEvent.getData(),treeCacheEvent.getOldData());
            }
        });
        client.watchPathChildrenNode("/", new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                ChildData childData = event.getData();
                String data = new String(childData.getData());
                LOG.info("childrenEvent: type:{},path:{},data:{},stat:{}",event.getType(), childData.getPath(), data, childData.getStat());
            }
        });
        String realPath = client.createPathData("/client", "value1".getBytes());
        LOG.info("realPath:{}",realPath);
        client.updatePathData(realPath,"newValue".getBytes());

        String newRealPath = client.createPathData("/client", "value2".getBytes());
        LOG.info("newRealPath:{}",newRealPath);
        client.updatePathData(newRealPath,"newValue2".getBytes());
        client.deletePath(realPath);
        client.deletePath(newRealPath);

        TimeUnit.SECONDS.sleep(5);
    }

}