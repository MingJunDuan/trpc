package com.netty.trpc.serialization.fury;

import com.netty.trpc.serialization.fury.domain.Address;
import com.netty.trpc.serialization.fury.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class FurySerializerTest {
    private FurySerializer serializer;

    @Before
    public void before(){
        serializer = new FurySerializer();
    }

    @Test
    public void serialize() {
        byte[] bytes = serializer.serialize(getUser());
        User user = serializer.deserialize(bytes, User.class);
        Assert.assertNotNull(user);

        Assert.assertEquals("Jack",user.getName());
        Assert.assertEquals("广东",user.getAddress().getProvince());
    }

    @Test
    public void deserialize() {
    }

    private User getUser(){
        User user = new User();
        user.setName("Jack");
        user.setAge(25);
        user.setPhones(Arrays.asList("01234567891"));

        Address address = new Address();
        address.setProvince("广东");
        user.setAddress(address);
        return user;
    }
}