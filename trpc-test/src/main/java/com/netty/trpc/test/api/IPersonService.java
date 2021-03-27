package com.netty.trpc.test.api;

import com.netty.trpc.test.service.Person;

import java.util.List;

public interface IPersonService {

    /**
     * 调用服务
     *
     * @param name
     * @param num
     * @return
     */
    List<Person> callPerson(String name, Integer num);
}
