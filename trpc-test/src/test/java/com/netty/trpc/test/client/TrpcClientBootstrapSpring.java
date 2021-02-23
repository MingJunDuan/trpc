package com.netty.trpc.test.client;

import com.netty.trpc.common.log.LOG;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-20 14:08
 */
public class TrpcClientBootstrapSpring {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("client-spring.xml");
        HelloServiceConsumer bean = applicationContext.getBean(HelloServiceConsumer.class);
        String result = bean.sayHello("Alice");
        LOG.info("result:{}", result);

        applicationContext.close();
    }
}
