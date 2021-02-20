package com.netty.trpc.test.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-20 14:08
 */
public class TrpcClientBootstrapSpring {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("client-spring.xml");
        applicationContext.refresh();
        HelloServiceConsumer bean = applicationContext.getBean(HelloServiceConsumer.class);
        //TODO 加上注解编程，使用FactoryBean将接口纳入Spring的管理
    }
}
