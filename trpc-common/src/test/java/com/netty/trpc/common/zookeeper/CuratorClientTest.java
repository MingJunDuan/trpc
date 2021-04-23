package com.netty.trpc.common.zookeeper;

import com.netty.trpc.BaseTest;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 10:03
 */
public class CuratorClientTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorClientTest.class);
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
        LOGGER.info(realPath);
        byte[] dataByte = client.getData(realPath);
        assertEquals(data, new String(dataByte));
        List<String> children = client.getChildren("/");
        assertEquals(1, children.size());
        children.forEach(p -> {
            LOGGER.info(p);
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
        LOGGER.info(realPath);
        //只能监听某个节点的变化，不能监听子节点的变化
        client.watchNode("/", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                LOGGER.info(event.toString());
            }
        });
        LOGGER.info("update data");
        client.updatePathData(realPath,"newValue".getBytes());
        LOGGER.info("create sub path");
        try {
            client.createPathData(realPath + "/subPath", "subPathValue".getBytes());
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        LOGGER.info("create new path");
        String newRealPath = client.createPathData(path, "value2".getBytes());
        LOGGER.info(newRealPath);
        client.deletePath(realPath);
        client.deletePath(newRealPath);
        TimeUnit.SECONDS.sleep(4);
    }

    @Test
    public void test_watchTreeNode() throws Exception {
        client.watchTreeNode("/", new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                LOGGER.info("type:{},data:{},oldData:{}",treeCacheEvent.getType(),treeCacheEvent.getData(),treeCacheEvent.getOldData());
            }
        });
        client.watchPathChildrenNode("/", new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                ChildData childData = event.getData();
                String data = new String(childData.getData());
                LOGGER.info("childrenEvent: type:{},path:{},data:{},stat:{}",event.getType(), childData.getPath(), data, childData.getStat());
            }
        });
        String realPath = client.createPathData("/client", "value1".getBytes());
        LOGGER.info("realPath:{}",realPath);
        client.updatePathData(realPath,"newValue".getBytes());

        String newRealPath = client.createPathData("/client", "value2".getBytes());
        LOGGER.info("newRealPath:{}",newRealPath);
        client.updatePathData(newRealPath,"newValue2".getBytes());
        client.deletePath(realPath);
        client.deletePath(newRealPath);
        List<String> children = client.getChildren("/");
        assertTrue(children.isEmpty());

        TimeUnit.SECONDS.sleep(5);
    }

}