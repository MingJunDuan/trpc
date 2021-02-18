package com.netty.trpc.test.client;

import com.netty.trpc.common.annotation.TrpcAutowired;
import com.netty.trpc.test.service.IHelloService;
import org.springframework.stereotype.Service;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 14:46
 */
@Service
public class HelloServiceConsumer {
    @TrpcAutowired(version = "1.0")
    private IHelloService helloService;

    public String sayHello(String name){
        return helloService.hello(name);
    }
}
