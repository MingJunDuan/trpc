package com.netty.trpc.common.codec;

import lombok.Data;

import java.io.Serializable;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:26
 */
@Data
public class TrpcRequest implements Serializable {
    private String requestId;
    private String interfaceName;
    private String methodName;
    private String version;
    private Class<?>[] parameterTypes;

    private boolean generic;
    private String[] parameterTypeStrList;

    private Object[] parameters;
}
