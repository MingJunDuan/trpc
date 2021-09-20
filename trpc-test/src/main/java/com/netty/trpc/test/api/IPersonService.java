package com.netty.trpc.test.api;

import com.netty.trpc.test.service.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public interface IPersonService {

    /**
     * 调用服务
     *
     * @param name
     * @param num
     * @return
     */
    List<Person> callPerson(String name, Integer num);


    Set<Person> getPersonSet(HashMap<String,Integer> map,LinkedList<String> names);
}
