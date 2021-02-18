package com.netty.trpc.test.service;

public interface IHelloService {
    String hello(String name);

    String hello(Person person);

    String hello(String name, Integer age);
}
