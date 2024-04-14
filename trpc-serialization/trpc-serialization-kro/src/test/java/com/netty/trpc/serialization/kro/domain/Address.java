package com.netty.trpc.serialization.kro.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {
    private String province;
}
