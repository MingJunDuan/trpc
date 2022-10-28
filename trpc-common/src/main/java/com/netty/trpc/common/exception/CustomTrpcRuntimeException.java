package com.netty.trpc.common.exception;

public class CustomTrpcRuntimeException extends RuntimeException {

    public CustomTrpcRuntimeException(String msg){
        this(msg,null);
    }

    public CustomTrpcRuntimeException(Throwable cause) {
        this(null, cause);
    }

    public CustomTrpcRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
