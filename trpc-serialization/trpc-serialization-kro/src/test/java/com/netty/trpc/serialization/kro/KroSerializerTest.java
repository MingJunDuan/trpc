package com.netty.trpc.serialization.kro;

import com.netty.trpc.serialization.api.Serializer;
import com.netty.trpc.serialization.kro.domain.Address;
import com.netty.trpc.serialization.kro.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class KroSerializerTest {
    private Serializer kroSerializer;

    @Before
    public void before(){
        kroSerializer = new KroSerializer();
    }

    @Test
    public void serialize() {
        byte[] bytes = kroSerializer.serialize(getUser());
        User user = kroSerializer.deserialize(bytes, User.class);
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