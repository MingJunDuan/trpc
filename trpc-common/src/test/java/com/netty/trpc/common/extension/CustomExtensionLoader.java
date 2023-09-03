package com.netty.trpc.common.extension;

@SPI(name = "customExtension1")
public interface CustomExtensionLoader {

    public String sayHello();
}
