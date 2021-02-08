package com.netty.trpc.server.service;

import com.netty.trpc.annotation.TrpcService;

@TrpcService(value = IHelloService.class, version = "1.0")
public class HelloServiceImpl implements IHelloService {

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello " + person.getFirstName() + " " + person.getLastName();
    }

    @Override
    public String hello(String name, Integer age) {
        return name + " is " + age;
    }
}
