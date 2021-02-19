package com.netty.trpc.test;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.client.handler.AsyncTrpcCallBack;
import com.netty.trpc.client.handler.TrpcFuture;
import com.netty.trpc.client.proxy.SerializableFunction;
import com.netty.trpc.client.proxy.TrpcService;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.test.service.IPersonService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 16:57
 */
public class TrpcClientCallbackTest extends BaseTest {
    private TrpcClient trpcClient;

    @Before
    public void before(){
        trpcClient = new TrpcClient(getRegistryAddress());
    }

    @Test
    public void test() throws Exception {
        TrpcService<IPersonService, SerializableFunction<IPersonService>> service = trpcClient.createAsyncService(IPersonService.class, "1.2");
        TrpcFuture trpcFuture = service.call("callPerson","Jerry", 3);
        trpcFuture.addCallback(new AsyncTrpcCallBack() {
            @Override
            public void success(Object result) {
                LOG.info("success,result:{}",result);
            }

            @Override
            public void fail(Exception e) {
                LOG.error("Error,exception:{}",e);
            }
        });

        Object result = trpcFuture.get(3, TimeUnit.SECONDS);
        LOG.info("result:{}",result);

        for (int i = 0; i < 10; i++) {
            result = doGetResult(service);
            LOG.info("result:{}",result);
        }
    }

    private Object doGetResult(TrpcService<IPersonService, SerializableFunction<IPersonService>> service) throws Exception {
        Object result;
        TrpcFuture future = service.call("callPerson", "Jerry", 4);
        result = future.get(500, TimeUnit.MILLISECONDS);
        return result;
    }

    @After
    public void after() throws Exception {
        trpcClient.destroy();
    }
}
