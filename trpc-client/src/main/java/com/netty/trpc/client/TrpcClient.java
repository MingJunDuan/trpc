package com.netty.trpc.client;

import com.netty.trpc.client.connect.ConnectionManager;
import com.netty.trpc.client.discovery.ServiceDiscovery;
import com.netty.trpc.client.proxy.ObjectProxy;
import com.netty.trpc.client.proxy.SerializableFunction;
import com.netty.trpc.client.proxy.TrpcService;
import com.netty.trpc.common.annotation.TrpcAutowired;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.common.util.threadpool.CallerRejectedExecutionHandler;
import com.netty.trpc.common.util.threadpool.EagerThreadPoolExecutor;
import com.netty.trpc.common.util.threadpool.NamedThreadFactory;
import com.netty.trpc.common.util.threadpool.TaskQueue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 9:46
 */
public class TrpcClient implements ApplicationContextAware, DisposableBean {

    private ServiceDiscovery serviceDiscovery;
    private static EagerThreadPoolExecutor threadPoolExecutor=new EagerThreadPoolExecutor(4,8,600L, TimeUnit.SECONDS, new TaskQueue<>(1000),
            new NamedThreadFactory("trpcClientEagerThread"),new CallerRejectedExecutionHandler());

    public TrpcClient(String registryAddress) {
        this.serviceDiscovery = new ServiceDiscovery(registryAddress);
    }

    public static <T> T createService(Class<T> interfaceClass,String version){
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass,version));
    }

    public static <T> TrpcService<T, SerializableFunction<T>> createAsyncService(Class<T> interfaces, String version){
        return new ObjectProxy<T>(interfaces,version);
    }

    public static void submit(Runnable runnable){
        threadPoolExecutor.submit(runnable);
    }

    public void stop(){
        threadPoolExecutor.shutdown();
        serviceDiscovery.stop();
        ConnectionManager.getInstance().stop();
    }

    @Override
    public void destroy() throws Exception {
        this.stop();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    TrpcAutowired trpcAutowired = field.getAnnotation(TrpcAutowired.class);
                    if (trpcAutowired!=null){
                        String version = trpcAutowired.version();
                        field.setAccessible(true);
                        field.set(bean,createService(field.getType(),version));
                    }
                } catch (IllegalAccessException e) {
                    LOG.error(e);
                }
            }
        }
    }
}
