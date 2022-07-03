package com.netty.trpc.test.api;

import com.netty.trpc.test.service.Person;

public interface IHelloService {

    String hello(String name);

    String concurent(int sleepTime);

    String hello(Person person);

    String hello(String name, Integer age);
}
