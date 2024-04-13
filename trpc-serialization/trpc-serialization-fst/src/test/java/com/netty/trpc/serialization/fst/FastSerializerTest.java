package com.netty.trpc.serialization.fst;

import com.netty.trpc.serialization.fst.domain.Address;
import com.netty.trpc.serialization.fst.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class FastSerializerTest {
    private FastSerializer fastSerializer;

    @Before
    public void before(){
        fastSerializer = new FastSerializer();
    }

    @Test
    public void testSerialize() {
        byte[] serialize = fastSerializer.serialize(getUser());
        User user = fastSerializer.deserialize(serialize, User.class);
        Assert.assertNotNull(user);

        Assert.assertEquals("Jack",user.getName());
        Assert.assertEquals("广东",user.getAddress().getProvince());
    }

    @Test
    public void testDeserialize() {
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