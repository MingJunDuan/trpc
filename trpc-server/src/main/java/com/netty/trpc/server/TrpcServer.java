package com.netty.trpc.server;

import com.netty.trpc.annotation.TrpcService;
import com.netty.trpc.server.core.TrpcNettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:01
 */
public class TrpcServer extends TrpcNettyServer implements ApplicationContextAware, InitializingBean, DisposableBean {

    public TrpcServer(String serverAddress,String registryAddress) {
        super(serverAddress,registryAddress);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(TrpcService.class);
        if (!serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                TrpcService nettyRpcService = serviceBean.getClass().getAnnotation(TrpcService.class);
                String interfaceName = nettyRpcService.value().getName();
                String version = nettyRpcService.version();
                super.addService(interfaceName, version, serviceBean);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        super.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();
    }

}
