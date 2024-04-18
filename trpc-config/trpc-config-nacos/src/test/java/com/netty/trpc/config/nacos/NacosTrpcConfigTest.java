package com.netty.trpc.config.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NacosTrpcConfigTest {
    private NacosTrpcConfig nacosTrpcConfig;

    @Before
    public void before() throws NacosException {
        nacosTrpcConfig = new NacosTrpcConfig("localhost");
    }

    @Test
    public void test_getConfig(){
        String value = nacosTrpcConfig.getProperty("redis");
        Assert.assertNotNull(value);
    }
}