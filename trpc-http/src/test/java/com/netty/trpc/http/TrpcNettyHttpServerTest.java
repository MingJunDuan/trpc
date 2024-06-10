package com.netty.trpc.http;

public class TrpcNettyHttpServerTest {

    public static void main(String[] args) {
        TrpcNettyHttpServer server = new TrpcNettyHttpServer();
        server.start();
    }
}