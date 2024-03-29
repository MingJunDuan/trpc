package com.netty.trpc.client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.netty.trpc.client.common.Holder;
import com.netty.trpc.client.connect.ConnectionManagerFactory;
import com.netty.trpc.client.discovery.ServiceDiscovery;
import com.netty.trpc.client.genericinvoke.GenericConfig;
import com.netty.trpc.client.genericinvoke.GenericReference;
import com.netty.trpc.client.proxy.GenericObjectProxy;
import com.netty.trpc.client.proxy.ObjectProxy;
import com.netty.trpc.client.proxy.SerializableFunction;
import com.netty.trpc.client.proxy.TrpcService;
import com.netty.trpc.common.annotation.TrpcAutowired;
import com.netty.trpc.common.util.threadpool.CallerRejectedExecutionHandler;
import com.netty.trpc.common.util.threadpool.EagerThreadPoolExecutor;
import com.netty.trpc.common.util.threadpool.NamedThreadFactory;
import com.netty.trpc.common.util.threadpool.TaskQueue;

import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 9:46
 */
public class TrpcClient implements ApplicationContextAware, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcClient.class);
    private static EagerThreadPoolExecutor threadPoolExecutor = new EagerThreadPoolExecutor(1, 8, 600L, TimeUnit.SECONDS, new TaskQueue<>(1000),
            new NamedThreadFactory("TrpcClientEagerThread"), new CallerRejectedExecutionHandler());
    private ServiceDiscovery serviceDiscovery;
    private GenericReference genericReference;

    public TrpcClient(String registryAddress) {
        this.serviceDiscovery = new ServiceDiscovery(registryAddress);
    }

    public static void submit(Runnable runnable) {
        threadPoolExecutor.submit(runnable);
    }

    public <T> T createService(Class<T> interfaceClass, String version) {
        T serviceBean = Holder.getServiceBean(interfaceClass, version);
        if (serviceBean == null) {
            InvocationHandler invocationHandler = null;
            if (GenericConfig.class.isAssignableFrom(interfaceClass) ){
                GenericObjectProxy<T> genericObjectProxy = new GenericObjectProxy<>(interfaceClass, version);
                genericObjectProxy.setGenericReference(genericReference);
                invocationHandler = genericObjectProxy;
            }else {
                invocationHandler = new ObjectProxy<T>(interfaceClass, version);
            }
            Object instance = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass}, invocationHandler);
            Holder.addServiceBean(interfaceClass, version, instance);
        }
        return Holder.getServiceBean(interfaceClass, version);
    }

    public <T> TrpcService<T, SerializableFunction<T>> createAsyncService(Class<T> interfaces, String version) {
        return new ObjectProxy<T>(interfaces, version);
    }

    public void stop() {
        threadPoolExecutor.shutdown();
        serviceDiscovery.stop();
        ConnectionManagerFactory.getConnectionManager().stop();
        ConnectionManagerFactory.getForkingConnectionManager().stop();
    }

    @Override
    public void destroy() throws Exception {
        this.stop();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Set<RpcServiceMetaInfo> rpcServiceMetaInfos=new HashSet<>();
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    TrpcAutowired trpcAutowired = field.getAnnotation(TrpcAutowired.class);
                    if (trpcAutowired != null) {
                        String version = trpcAutowired.version();
                        field.setAccessible(true);
                        RpcServiceMetaInfo rpcServiceMetaInfo = new RpcServiceMetaInfo();
                        rpcServiceMetaInfo.setServiceName(field.getType().getCanonicalName());
                        rpcServiceMetaInfo.setVersion(version);
                        rpcServiceMetaInfos.add(rpcServiceMetaInfo);
                        field.set(bean, createService(field.getType(), version));
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        serviceDiscovery.subscribe(rpcServiceMetaInfos);
    }

    public void setGenericReference(GenericReference genericReference) {
        this.genericReference = genericReference;
    }
}
