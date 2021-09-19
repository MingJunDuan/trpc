package com.netty.trpc.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import com.netty.trpc.client.connect.ConnectionManagerFactory;
import com.netty.trpc.client.faulttolerance.FailBackInvoker;
import com.netty.trpc.client.faulttolerance.Invoker;
import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.client.handler.TrpcFuture;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.util.ServiceUtil;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 13:54
 */
public class ObjectProxy<T> implements InvocationHandler,TrpcService<T,SerializableFunction<T>> {
    private static Invoker invoker = new FailBackInvoker();
    private Class<T> clazz;
    private String version;

    public ObjectProxy(Class<T> clazz, String version) {
        this.clazz = clazz;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        if (Object.class==declaringClass){
            String name = getMethodName(method);
            if ("equals".equals(name)){
                return proxy==args[0];
            }else if ("hashCode".equals(name)){
                return System.identityHashCode(proxy);
            }else if ("toString".equals(name)){
                return proxy.getClass().getName()+"@"+Integer.toHexString(System.identityHashCode(proxy))+
                        ", with InvocationHandler " + this;
            }else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }
        TrpcRequest request = new TrpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setInterfaceName(getInterfaceName(method));
        request.setMethodName(getMethodName(method));
        request.setParameterTypes(getParameterTypes(method));
        request.setParameters(getArgs(args));
        request.setVersion(version);

        extend(request);

        String serviceKey = ServiceUtil.serviceKey(getInterfaceName(method), version);

        Object result = invoker.invoke(serviceKey, request);
        return result;
    }

    protected Object[] getArgs(Object[] args) {
        return args;
    }

    protected Class<?>[] getParameterTypes(Method method) {
        return method.getParameterTypes();
    }

    protected String getMethodName(Method method) {
        return method.getName();
    }

    protected String getInterfaceName(Method method) {
        return method.getDeclaringClass().getName();
    }

    protected void extend(TrpcRequest request){ }

    @Override
    public TrpcFuture call(String funcName, Object... args) throws Exception {
        String serviceKey = ServiceUtil.serviceKey(this.clazz.getName(), version);
        TrpcClientHandler handler = ConnectionManagerFactory.getConnectionManager().chooseHandler(serviceKey);
        TrpcRequest request=createRequest(this.clazz.getName(),funcName,args);
        TrpcFuture trpcFuture = handler.sendRequest(request);
        return trpcFuture;
    }

    private TrpcRequest createRequest(String className, String methodName, Object[] args) {
        TrpcRequest request = new TrpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setInterfaceName(className);
        request.setMethodName(methodName);
        request.setParameters(args);
        request.setVersion(version);
        Class[] parameterTypes=new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i]=getClassType(args[i]);
        }
        request.setParameterTypes(parameterTypes);
        return request;
    }

    private Class getClassType(Object arg) {
        return arg.getClass();
    }

    @Override
    public TrpcFuture call(SerializableFunction<T> tSerializableFunction, Object... args) {
        throw new UnsupportedOperationException();
    }
}
