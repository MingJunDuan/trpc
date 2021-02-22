package com.netty.trpc.client.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-20 14:57
 */
public class TrpcNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("reference", new TrpcReferenceParser());
    }
}
