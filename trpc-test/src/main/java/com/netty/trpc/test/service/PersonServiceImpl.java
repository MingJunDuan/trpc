package com.netty.trpc.test.service;

import com.netty.trpc.common.annotation.TrpcService;
import com.netty.trpc.test.api.IHelloService;
import com.netty.trpc.test.api.IPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 */
@TrpcService(value = IPersonService.class,version = "1.2")
public class PersonServiceImpl implements IPersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);
    @Resource(name = "helloService2")
    private IHelloService helloService;

    @Override
    public List<Person> callPerson(String name, Integer num) {
        LOGGER.info("callPerson method");
        List<Person> persons = new ArrayList<>(num);
        for (int i = 0; i < num; ++i) {
            String result = helloService.hello(name + i);
            Person person = new Person(Integer.toString(i), result);
            person.setAddress(result);
            persons.add(person);
        }
        return persons;
    }

    @Override
    public Set<Person> getPersonSet(HashMap<String, Integer> map, LinkedList<String> names) {
        LOGGER.info("getPersonSet");

        Set<Person> persons = new HashSet<>();
        for (int i = 0; i < names.size(); ++i) {
            String result = helloService.hello(names.get(i)+map.get(names.get(i)));
            persons.add(new Person(Integer.toString(i), result));
        }
        return persons;
    }
}
