package com.netty.trpc.serialization.kro.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class User implements Serializable {
    private String name;
    private int age;
    private Address address;
    private List<String> phones;
}
