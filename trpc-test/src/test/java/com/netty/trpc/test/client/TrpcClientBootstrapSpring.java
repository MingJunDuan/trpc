package com.netty.trpc.test.client;

import com.netty.trpc.common.log.LOG;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-20 14:08
 */
public class TrpcClientBootstrapSpring {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("client-spring.xml");
        HelloServiceConsumer bean = applicationContext.getBean(HelloServiceConsumer.class);
        for (int i = 0; i < 6; i++) {
            String result = bean.sayHello("Alice");
            LOG.info("result:{}", result);
        }
        TimeUnit.DAYS.sleep(2);
        applicationContext.close();
    }
}
