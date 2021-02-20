package com.netty.trpc.test.service;

import com.netty.trpc.common.annotation.TrpcService;
import com.netty.trpc.test.api.IHelloService;
import org.springframework.stereotype.Service;

@TrpcService(value = IHelloService.class, version = "2.0")
@Service("helloService2")
public class HelloServiceImpl2 implements IHelloService {

    @Override
    public String hello(String name) {
        return "Hello2 " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello2 " + person.getFirstName() + " " + person.getLastName();
    }

    @Override
    public String hello(String name, Integer age) {
        return name + " 2 is " + age;
    }
}
