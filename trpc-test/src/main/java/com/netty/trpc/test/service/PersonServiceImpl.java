package com.netty.trpc.test.service;

import com.netty.trpc.common.annotation.TrpcService;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.test.interfaces.IHelloService;
import com.netty.trpc.test.interfaces.IPersonService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxiaoxun on 2016-03-10.
 */
@TrpcService(value = IPersonService.class,version = "1.2")
public class PersonServiceImpl implements IPersonService {
    @Resource(name = "helloService2")
    private IHelloService helloService;

    @Override
    public List<Person> callPerson(String name, Integer num) {
        LOG.info("callPerson method");
        List<Person> persons = new ArrayList<>(num);
        for (int i = 0; i < num; ++i) {
            String result = helloService.hello(name + i);
            persons.add(new Person(Integer.toString(i), result));
        }
        return persons;
    }
}
