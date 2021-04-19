package com.netty.trpc.server.service;

import java.util.List;

/**
 */
public interface IPersonService {
    List<Person> callPerson(String name, Integer num);
}
