package com.netty.trpc.test.client;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.test.BaseTest;
import com.netty.trpc.test.api.IHelloService;
import com.netty.trpc.test.api.IPersonService;
import com.netty.trpc.test.service.Person;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 14:50
 */
public class TrpcClientTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcClientTest.class);
    private TrpcClient trpcClient;

    @Before
    public void before() {
        trpcClient = new TrpcClient(getRegistryAddress());
    }

    @Test
    public void test() throws InterruptedException {
        IHelloService helloService = trpcClient.createService(IHelloService.class, "1.0");
        String jack = "Jack";
        String result = helloService.hello(jack);
        Assert.assertEquals("Hello " + jack, result);
        LOGGER.info(result);

        String tom = "Tom";
        result = helloService.hello(tom);
        Assert.assertEquals("Hello " + tom, result);
        LOGGER.info(result);
        TimeUnit.SECONDS.sleep(3);
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
