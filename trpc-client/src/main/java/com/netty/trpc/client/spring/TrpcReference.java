package com.netty.trpc.client.spring;

import com.netty.trpc.client.TrpcClient;
import com.netty.trpc.common.log.LOG;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-20 14:59
 */
@Data
public class TrpcReference implements ApplicationContextAware, FactoryBean, InitializingBean {
    private String interfaceName;
    private String version;
    private String protocol;
    private TrpcClient trpcClient;

    @Override
    public Object getObject() throws Exception {
        return trpcClient.createService(this.getObjectType(), version);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return this.getClass().getClassLoader().loadClass(interfaceName);
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found exception", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isBlank(interfaceName)) {
            throw new IllegalStateException("Interface name can not blank!");
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.trpcClient = applicationContext.getBean(TrpcClient.class);
    }
}
