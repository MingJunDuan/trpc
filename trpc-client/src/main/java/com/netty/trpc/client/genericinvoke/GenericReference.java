/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.client.genericinvoke;

import java.util.Objects;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author dmj1161859184@126.com 2021-09-19 23:07
 * @version 1.0
 * @since 1.0
 */
public class GenericReference extends GenericReferenceBase implements InitializingBean {

    public GenericConfig get(){
        trpcClient.setGenericReference(this);
        return trpcClient.createService(GenericConfig.class,this.version);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Objects.requireNonNull(this.interfaceName);
        Objects.requireNonNull(this.version);
        Objects.requireNonNull(this.trpcClient);
    }
}
