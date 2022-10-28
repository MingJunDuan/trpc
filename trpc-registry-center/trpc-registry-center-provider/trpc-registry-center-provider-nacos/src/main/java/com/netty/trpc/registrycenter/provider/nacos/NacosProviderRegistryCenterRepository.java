package com.netty.trpc.registrycenter.provider.nacos;

import java.util.List;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netty.trpc.common.extension.SPI;
import com.netty.trpc.common.util.RegistryUtil;
import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;
import com.netty.trpc.registrycenter.provider.api.ProviderRegistryCenterRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mjduan
 * @version 1.0
 * @date 2021-12-01 12:31
 */
@SPI(name = "nacos")
public class NacosProviderRegistryCenterRepository implements ProviderRegistryCenterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosProviderRegistryCenterRepository.class);
    static final String applicationName = "applicationName";
    private static final String clusterName = "trpc-cluster";
    private NamingService namingService;

    @Override
    public void init(RegistryCenterMetadata metadata) {
        String nacosServerList = RegistryUtil.registryAddress(metadata.getServerList());
        try {
            namingService = NamingFactory.createNamingService(nacosServerList);
        } catch (NacosException e) {
            LOGGER.error("Init nacos naming server exception", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registry(RegistryMetadata metadata) {
        CallbackHandle callbackHandle = new CallbackHandle() {

            @Override
            public void handle(Instance instance) {
                try {
                    namingService.registerInstance(instance.getServiceName(), instance);
                } catch (NacosException e) {
                    LOGGER.error("Registry instance exception", e);
                    throw new RuntimeException(e);
                }
            }
        };

        doHandle(metadata, callbackHandle);
    }

    @Override
    public void unregistry(RegistryMetadata metadata) {
        CallbackHandle callbackHandle = new CallbackHandle() {

            @Override
            public void handle(Instance instance) {
                try {
                    namingService.deregisterInstance(instance.getServiceName(), instance);
                } catch (NacosException e) {
                    LOGGER.error("Registry instance exception", e);
                    throw new RuntimeException(e);
                }
            }
        };
        doHandle(metadata, callbackHandle);
    }

    interface CallbackHandle {

        void handle(Instance instance);
    }

    private void doHandle(RegistryMetadata metadata, CallbackHandle callbackHandle) {
        int port = metadata.getPort();
        String host = metadata.getHost();
        List<RpcServiceMetaInfo> serviceInfoList = metadata.getServiceInfoList();

        Instance instance = new Instance();
        instance.setIp(host);
        instance.setPort(port);
        instance.setHealthy(true);
        instance.setWeight(100);
        instance.setEphemeral(true);
        instance.setClusterName(clusterName);

        for (RpcServiceMetaInfo rpcServiceMetaInfo : serviceInfoList) {
            instance.setServiceName(rpcServiceMetaInfo.getServiceName()+":"+rpcServiceMetaInfo.getVersion());
            callbackHandle.handle(instance);
        }
    }
}
