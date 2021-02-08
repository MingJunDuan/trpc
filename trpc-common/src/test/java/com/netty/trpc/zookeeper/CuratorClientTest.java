package com.netty.trpc.zookeeper;

import com.netty.trpc.BaseTest;
import com.netty.trpc.log.LOG;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 10:03
 */
public class CuratorClientTest extends BaseTest {
    private CuratorClient client;

    @Before
    public void before() {
        client = new CuratorClient(zkConnectStr, zkNamespaceStr);
    }

    @Test
    public void test_connect() throws Exception {
        String path = "/clent1";
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
}