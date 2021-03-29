package com.netty.trpc.test.service;

import com.netty.trpc.common.annotation.TrpcService;
import com.netty.trpc.test.api.IHelloService;
import org.springframework.stereotype.Service;

@TrpcService(value = IHelloService.class, version = "1.0")
@Service("helloService")
public class HelloServiceImpl implements IHelloService {

    @Override
    public String hello(String name) {
        if ("jack".equalsIgnoreCase(name)){
            throw new IllegalStateException("有意的抛出异常");
        }
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
