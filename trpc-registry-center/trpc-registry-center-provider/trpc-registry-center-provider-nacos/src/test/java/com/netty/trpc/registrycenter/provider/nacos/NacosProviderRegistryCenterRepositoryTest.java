package com.netty.trpc.registrycenter.provider.nacos;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmj1161859184@126.com 2022-09-28 00:47
 * @version 1.0
 * @since 1.0
 */
public class NacosProviderRegistryCenterRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosProviderRegistryCenterRepositoryTest.class);
    private NacosProviderRegistryCenterRepository repository;

    @Before
    public void before() {
        repository = new NacosProviderRegistryCenterRepository();
    }

    @Test
    public void init() {
        RegistryCenterMetadata metadata = new RegistryCenterMetadata();
        metadata.setServerList("localhost:8848");
        repository.init(metadata);
        LOGGER.info("init done");
    }

    @Test
    public void registry() throws InterruptedException {
        init();

        RegistryMetadata metadata = new RegistryMetadata();
        metadata.setHost("localhost");
        metadata.setPort(8890);
        Properties properties = new Properties();
        properties.put(NacosProviderRegistryCenterRepository.applicationName, "trpc-provider");
        metadata.setProperties(properties);
        repository.registry(metadata);
        LOGGER.info("registry done");
        TimeUnit.MINUTES.sleep(5);
    }

    @Test
    public void unregistry() {
        init();

        RegistryMetadata metadata = new RegistryMetadata();
        metadata.setHost("localhost");
        metadata.setPort(8890);
        Properties properties = new Properties();
        properties.put(NacosProviderRegistryCenterRepository.applicationName, "trpc-provider");
        metadata.setProperties(properties);
        repository.registry(metadata);

//        RegistryMetadata metadata = new RegistryMetadata();
//        metadata.setHost("localhost");
//        metadata.setPort(8890);
//        Properties properties = new Properties();
        properties.put(NacosProviderRegistryCenterRepository.applicationName, "trpc-provider");
        metadata.setProperties(properties);
        repository.unregistry(metadata);
        LOGGER.info("unregistry done");
    }
}