package com.netty.trpc.common.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegistryUtilTest {

    @Test
    public void protocol_zookeeper() {
        String expectedProtocol = "zookeeper";
        String registryProtocol = expectedProtocol + "://localhost:2181";
        String protocol = RegistryUtil.protocol(registryProtocol);
        Assert.assertEquals(expectedProtocol, protocol);
        System.out.println(protocol);
    }

    @Test
    public void registryAddress(){
        String expectedProtocol = "zookeeper";
        String zkAddress="localhost:2181";
        String registryProtocol = expectedProtocol + "://"+zkAddress;
        String registryAddress = RegistryUtil.registryAddress(registryProtocol);
        Assert.assertEquals(zkAddress, registryAddress);
    }
}