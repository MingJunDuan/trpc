package com.netty.trpc.test.client;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.test.BaseTest;
import com.netty.trpc.test.api.IHelloService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 14:50
 */
public class TrpcClientTest extends BaseTest {
    private TrpcClient trpcClient;

    @Before
    public void before(){
        trpcClient = new TrpcClient(getRegistryAddress());
    }

    @Test
    public void test() throws InterruptedException {
        IHelloService helloService = trpcClient.createService(IHelloService.class, "1.0");
        String jack = "Jack";
        String result = helloService.hello(jack);
        Assert.assertEquals("Hello "+jack,result);
        LOG.info(result);

        String tom = "Tom";
        result = helloService.hello(tom);
        Assert.assertEquals("Hello "+tom,result);
        LOG.info(result);
        TimeUnit.SECONDS.sleep(3);
    }

    @After
    public void after() throws Exception {
        trpcClient.destroy();
    }
    
}
