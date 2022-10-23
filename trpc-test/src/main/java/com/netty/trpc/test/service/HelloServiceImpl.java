package com.netty.trpc.test.service;

import java.util.concurrent.TimeUnit;

import com.netty.trpc.common.annotation.TrpcService;
import com.netty.trpc.test.api.IHelloService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@TrpcService(value = IHelloService.class, version = "1.0")
@Service("helloService")
public class HelloServiceImpl implements IHelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(String name) {
        LOGGER.info("Rec:{}",name);
        return "Hello " + name;
    }

    @Override
    public String concurent(int sleepTime) {
        LOGGER.info("start sleep {}s",sleepTime);
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("end sleep");
        return "Hello"+sleepTime;
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
