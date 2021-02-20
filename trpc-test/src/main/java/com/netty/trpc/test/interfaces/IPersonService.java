package com.netty.trpc.test.interfaces;

import com.netty.trpc.test.service.Person;

import java.util.List;

/**
 * Created by luxiaoxun on 2016-03-10.
 */
public interface IPersonService {
    List<Person> callPerson(String name, Integer num);
}
