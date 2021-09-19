/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.test.client;

import java.util.List;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.client.genericinvoke.GenericConfig;
import com.netty.trpc.client.genericinvoke.GenericReference;
import com.netty.trpc.test.BaseTest;
import com.netty.trpc.test.api.IPersonService;
import com.netty.trpc.test.service.Person;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmj1161859184@126.com 2021-09-19 23:53
 * @version 1.0
 * @since 1.0
 */
public class TrpcClientGenericTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcClientTest.class);
    private TrpcClient trpcClient;

    @Before
    public void before() {
        trpcClient = new TrpcClient(getRegistryAddress());
    }

    //泛化调用
    @Test
    public void test_generic() throws InterruptedException {
        GenericReference genericReference = new GenericReference();
        genericReference.setTrpcClient(trpcClient);
        genericReference.setInterfaceName("com.netty.trpc.test.api.IPersonService");
        genericReference.setVersion("1.2");
        genericReference.setMethodName("callPerson");
        String[] parameterTypes = {"java.lang.String", "java.lang.Integer"};
        genericReference.setParameterTypes(parameterTypes);

        GenericConfig genericConfig = genericReference.get();
        Object[] args = {"Jack", 3};
        for (int i = 0; i < 10; i++) {
            Object result = genericConfig.$invoke(args);
            LOGGER.info("result:{}",result.toString());
        }
    }

    @Test
    public void test_personService() throws InterruptedException {
        IPersonService helloService = trpcClient.createService(IPersonService.class, "1.2");
        String jack = "Jack";
        List<Person> personList = helloService.callPerson(jack, 3);

        LOGGER.info(personList.toString());
    }

    @After
    public void after() throws Exception {
        trpcClient.destroy();
    }


}
