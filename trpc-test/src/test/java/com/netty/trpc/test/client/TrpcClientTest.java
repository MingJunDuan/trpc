package com.netty.trpc.test.client;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.test.BaseTest;
import com.netty.trpc.test.api.IHelloService;
import com.netty.trpc.test.api.IPersonService;
import com.netty.trpc.test.service.Person;
import lombok.AllArgsConstructor;

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
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(10,30,60,TimeUnit.SECONDS,new LinkedBlockingDeque<>());

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
        for (int i = 0; i < 10; i++) {
            List<Person> personList = helloService.callPerson(jack, 3);
            LOGGER.info(personList.toString());
        }
    }

    @Test
    public void test_personService_threadPool() throws InterruptedException {
        IPersonService helloService = trpcClient.createService(IPersonService.class, "1.2");
        String jack = "Jack";
        AtomicInteger count=new AtomicInteger(0);
        int total=100000;
        for (int i = 0; i < total; i++) {
            executor.submit(new CustomThread(helloService,jack+i,3,count));
        }
        while (count.get()<total){
            Thread.yield();
        }
        executor.shutdownNow();
    }

    @AllArgsConstructor
    static class CustomThread extends Thread{
        private static final Logger LOGGER = LoggerFactory.getLogger(CustomThread.class);
        private IPersonService personService;
        private String name;
        private int count;
        private AtomicInteger total;

        @Override
        public void run() {
            List<Person> personList = personService.callPerson(name, count);
            LOGGER.info(personList.toString());
            total.incrementAndGet();
        }
    }

    @After
    public void after() throws Exception {
        trpcClient.destroy();
    }
}
