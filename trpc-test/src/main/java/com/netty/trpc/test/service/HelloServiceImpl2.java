package com.netty.trpc.test.service;

import com.netty.trpc.common.annotation.TrpcService;

@TrpcService(value = IHelloService.class, version = "1.0")
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
