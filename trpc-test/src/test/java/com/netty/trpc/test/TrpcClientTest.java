package com.netty.trpc.test;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.test.service.IHelloService;
import org.junit.Before;
import org.junit.Test;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 14:50
 */
public class TrpcClientTest {
    private TrpcClient trpcClient;

    @Before
    public void before(){
        trpcClient = new TrpcClient("localhost:2181");
    }

    @Test
    public void test(){
        IHelloService helloService = trpcClient.createService(IHelloService.class, "1.0");
        String result = helloService.hello("Jack");
        LOG.info(result);
    }
}
