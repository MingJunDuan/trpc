package com.netty.trpc.server.service;

import java.util.List;

/**
 * Created by luxiaoxun on 2016-03-10.
 */
public interface IPersonService {
    List<Person> callPerson(String name, Integer num);
}
