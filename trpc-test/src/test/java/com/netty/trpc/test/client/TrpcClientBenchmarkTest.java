package com.netty.trpc.test.client;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.common.util.threadpool.NamedThreadFactory;
import com.netty.trpc.test.BaseTest;
import com.netty.trpc.test.api.IHelloService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 14:50
 */
public class TrpcClientBenchmarkTest extends BaseTest {
    private static final int threadNum = 16;
    private TrpcClient trpcClient;
    private final LongAdder count = new LongAdder();
    private final int requestNumPeerThread = 100;
    private int taskNum = 100;
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), new NamedThreadFactory("BenchmarkClientThread"));
    private int totalCount=taskNum*requestNumPeerThread;

    @Before
    public void before(){
        trpcClient = new TrpcClient(getRegistryAddress());
    }

    @Test
    public void test() throws InterruptedException {
        CountDownLatch countDownLatch=new CountDownLatch(taskNum);
        for (int i = 0; i < taskNum; i++) {
            threadPoolExecutor.execute(new ClientThread(trpcClient,countDownLatch));
        }
        threadPoolExecutor.shutdown();
        countDownLatch.await(30,TimeUnit.SECONDS);
        Assert.assertEquals(totalCount, count.intValue());
    }

    @After
    public void after() throws Exception {
        trpcClient.destroy();
    }

    class ClientThread implements Runnable{
        private TrpcClient trpcClient;
        private CountDownLatch countDownLatch;

        public ClientThread(TrpcClient trpcClient,CountDownLatch countDownLatch) {
            this.trpcClient = trpcClient;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            IHelloService helloService = trpcClient.createService(IHelloService.class, "1.0");
            for (int i = 0; i < requestNumPeerThread; i++) {
                String jack = "Jack";
                String result = helloService.hello(jack);
                Assert.assertEquals("Hello "+jack,result);
                LOG.info(result);

                String tom = "Tom";
                result = helloService.hello(tom);
                Assert.assertEquals("Hello "+tom,result);
                LOG.info(result);
                count.add(1);
            }
            countDownLatch.countDown();
        }
    }
}
