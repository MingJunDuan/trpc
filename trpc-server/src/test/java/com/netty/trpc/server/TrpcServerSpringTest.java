package com.netty.trpc.server;


import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 15:33
 */
public class TrpcServerSpringTest {

    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("server-spring.xml");
        context.refresh();

    }
}