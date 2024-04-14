package com.netty.trpc.test.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-20 14:08
 */
public class TrpcClientBootstrapSpring {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcClientBootstrapSpring.class);

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("client-spring.xml");
        HelloServiceConsumer bean = applicationContext.getBean(HelloServiceConsumer.class);
        for (int i = 0; i < 1; i++) {
            String result = bean.sayHello("Jack");
            LOGGER.info("result:{}", result);
            TimeUnit.SECONDS.sleep(1);
        }
        //TimeUnit.DAYS.sleep(2);
        applicationContext.close();
    }
}
