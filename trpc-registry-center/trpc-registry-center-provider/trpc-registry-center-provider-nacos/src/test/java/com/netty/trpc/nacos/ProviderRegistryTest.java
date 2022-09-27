/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.nacos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Cluster;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmj1161859184@126.com 2022-09-28 00:15
 * @version 1.0
 * @since 1.0
 */
public class ProviderRegistryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderRegistryTest.class);
    //nacos server
    private String serveAddr = "localhost:8848";
    private String serviceName = "trp";

    @Test
    public void test_registry() throws NacosException, InterruptedException {
        NamingService naming = NamingFactory.createNamingService(serveAddr);
        naming.registerInstance(serviceName, "11.11.11.11", 8888);
        naming.registerInstance(serviceName, "11.11.11.12", 8888);


        Instance instance = new Instance();
        instance.setIp("55.55.55.55");
        instance.setPort(9999);
        instance.setHealthy(true);
        instance.setWeight(2.0);
        //这个数据是存储在文件中的,而不是数据库,地址是data/naming/data/public/,所以即使注册中心挂了重启也会有数据
        instance.setEphemeral(true);
        instance.setServiceName("MyServiceName");
        Map<String, String> instanceMeta = new HashMap<>();
        instanceMeta.put("site", "et2");

        instance.setMetadata(instanceMeta);

        Service service = new Service("nacos.test.4");
        service.setAppName("nacos-naming");

        service.setProtectThreshold(0.8F);
        Map<String, String> serviceMeta = new HashMap<>();
        serviceMeta.put("symmetricCall", "true");
        service.setMetadata(serviceMeta);

        Cluster cluster = new Cluster();
        cluster.setName("TEST5");
        Map<String, String> clusterMeta = new HashMap<>();
        clusterMeta.put("xxx", "yyyy");
        cluster.setMetadata(clusterMeta);

        instance.setClusterName("ClusterNameTest3");

        naming.registerInstance("nacos.test.4", instance);

        LOGGER.info("registry done");
        int i=0;
        while (true) {
            TimeUnit.MILLISECONDS.sleep(3000);
            naming.registerInstance(serviceName, "11.11.11.1"+i, 8888);
            i++;
        }
    }

    @Test
    public void test_subscribe() throws NacosException, InterruptedException {
        NamingService naming = NamingFactory.createNamingService(serveAddr);
        naming.subscribe(serviceName, event -> {
            if (event instanceof NamingEvent) {
                List<Instance> instances = ((NamingEvent) event).getInstances();
                for (Instance instance : instances) {
                    LOGGER.info("{}",instance);
                }
                System.out.println(instances.size());
            }
        });

        while (true) {
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
