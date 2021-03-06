package com.netty.trpc.test.client;

import java.util.List;
import java.util.concurrent.CountDownLatch;
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
        String jack = "Jack2";
        String result = helloService.hello(jack);
        Assert.assertEquals("Hello " + jack, result);
        LOGGER.info(result);
        LOGGER.info("----------------------------");

        while (true) {
            String tom = "Tom";
            result = helloService.hello(tom);
            Assert.assertEquals("Hello " + tom, result);
            LOGGER.info(result);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void test_concurrent() throws InterruptedException {
        IHelloService helloService = trpcClient.createService(IHelloService.class, "1.0");
        String tmp="Hello";
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        threadPoolExecutor.prestartAllCoreThreads();
        CountDownLatch countDownLatch=new CountDownLatch(1);
        CountDownLatch allDone=new CountDownLatch(4);

        long startTime = System.currentTimeMillis();
        LOGGER.info("Start");
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    int sleepTime = 3;
                    String result = helloService.concurent(sleepTime);
                    Assert.assertEquals(tmp+sleepTime,result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    allDone.countDown();
                }
            }
        });
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    int sleepTime = 2;
                    String result = helloService.concurent(sleepTime);
                    Assert.assertEquals(tmp+sleepTime,result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    allDone.countDown();
                }
            }
        });
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    int sleepTime = 4;
                    String result = helloService.concurent(sleepTime);
                    Assert.assertEquals(tmp+sleepTime,result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    allDone.countDown();
                }
            }
        });
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    int sleepTime = 5;
                    String result = helloService.concurent(sleepTime);
                    Assert.assertEquals(tmp+sleepTime,result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    allDone.countDown();
                }
            }
        });
        countDownLatch.countDown();
        allDone.await();
        LOGGER.info("End, {}s",(System.currentTimeMillis()-startTime)/1000);
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
        int total=1000;
        for (int i = 0; i < total; i++) {
            executor.submit(new CustomThread(helloService,jack+i,3,count));
        }
        while (count.get()<total){
            Thread.yield();
        }
        executor.shutdownNow();
        Assert.assertEquals(total,count.get());
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
