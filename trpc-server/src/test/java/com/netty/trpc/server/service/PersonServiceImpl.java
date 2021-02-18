package com.netty.trpc.server.service;

import com.netty.trpc.common.annotation.TrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxiaoxun on 2016-03-10.
 */
@TrpcService(IPersonService.class)
public class PersonServiceImpl implements IPersonService {
    @Autowired
    private IHelloService helloService;

    @Override
    public List<Person> callPerson(String name, Integer num) {
        List<Person> persons = new ArrayList<>(num);
        for (int i = 0; i < num; ++i) {
            String result = helloService.hello(name + i);
            persons.add(new Person(Integer.toString(i), result));
        }
        return persons;
    }
}
