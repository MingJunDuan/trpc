package com.netty.trpc.test.server;

import com.netty.trpc.common.log.LOG;
import com.netty.trpc.server.TrpcServer;
import com.netty.trpc.test.interfaces.IHelloService;
import com.netty.trpc.test.interfaces.IPersonService;
import com.netty.trpc.test.service.HelloServiceImpl;
import com.netty.trpc.test.service.HelloServiceImpl2;
import com.netty.trpc.test.service.PersonServiceImpl;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 14:42
 */
public class TrpcServerBootstrapMannul {

    public static void main(String[] args){
        String serverAddress = "127.0.0.1:18878";
        String registryAddress = "localhost:2181";
        TrpcServer rpcServer = new TrpcServer(serverAddress, registryAddress);
        IHelloService helloService1 = new HelloServiceImpl();
        rpcServer.addService(IHelloService.class.getName(), "1.0", helloService1);
        IHelloService helloService2 = new HelloServiceImpl2();
        rpcServer.addService(IHelloService.class.getName(), "2.0", helloService2);
        IPersonService personService = new PersonServiceImpl();
        rpcServer.addService(IPersonService.class.getName(), "", personService);
        try {
            rpcServer.start();
        } catch (Exception ex) {
            LOG.error("Exception: {}", ex);
        }
    }
}
