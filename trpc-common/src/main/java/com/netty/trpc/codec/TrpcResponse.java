package com.netty.trpc.codec;

import lombok.Data;

import java.io.Serializable;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 13:26
 */
@Data
public class TrpcResponse implements Serializable {
    private String requestId;
    private String error;
    private Object result;
}
