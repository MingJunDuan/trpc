package com.netty.trpc.test.tps;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 14:41
 */
public class TrpcServerBootstrapSpring {

    public static void main(String[] args){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("server-spring.xml");
        applicationContext.refresh();
    }

}
