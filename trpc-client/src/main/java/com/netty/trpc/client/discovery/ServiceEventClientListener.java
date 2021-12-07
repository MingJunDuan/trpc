package com.netty.trpc.client.discovery;

import java.util.List;

import com.netty.trpc.client.connect.ConnectionManagerFactory;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.consumer.api.ServiceEventListener;

/**
 * @author mjduan
 * @version 1.0
 * @date 2021-12-06 12:30
 */
public class ServiceEventClientListener implements ServiceEventListener {

    @Override
    public <T> void publish(T event) {
        List<RegistryMetadata> registryMetadataList = (List<RegistryMetadata>) event;
        ConnectionManagerFactory.getConnectionManager().updateConnectedServer(registryMetadataList);
    }
}
