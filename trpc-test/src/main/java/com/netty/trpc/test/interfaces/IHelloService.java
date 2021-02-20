package com.netty.trpc.test.interfaces;

import com.netty.trpc.test.service.Person;

public interface IHelloService {
    String hello(String name);

    String hello(Person person);

    String hello(String name, Integer age);
}
