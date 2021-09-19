package com.netty.trpc.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import com.netty.trpc.client.connect.ConnectionManagerFactory;
import com.netty.trpc.client.faulttolerance.FailBackInvoker;
import com.netty.trpc.client.faulttolerance.Invoker;
import com.netty.trpc.client.genericinvoke.GenericReference;
import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.handler.TrpcFuture;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.util.ServiceUtil;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 13:54
 */
public class GenericObjectProxy<T> extends ObjectProxy<T> {
    private GenericReference genericReference;

    public GenericObjectProxy(Class<T> clazz, String version) {
        super(clazz, version);
    }

    public void setGenericReference(GenericReference genericReference) {
        this.genericReference = genericReference;
    }

    @Override
    protected String getInterfaceName(Method method) {
        return genericReference.getInterfaceName();
    }

    @Override
    protected String getMethodName(Method method) {
        return genericReference.getMethodName();
    }

    @Override
    protected Object[] getArgs(Object[] args) {
        if (args!=null && args.length>0){
            return (Object[]) args[0];
        }
        return args;
    }

    @Override
    protected Class<?>[] getParameterTypes(Method method) {
        return null;
    }

    @Override
    protected void extend(TrpcRequest request) {
        request.setGeneric(true);
        request.setParameterTypeStrList(genericReference.getParameterTypes());
    }

}
