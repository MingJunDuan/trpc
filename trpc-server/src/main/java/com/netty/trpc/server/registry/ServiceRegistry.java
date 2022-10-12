package com.netty.trpc.server.registry;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netty.trpc.common.util.ServiceUtil;
import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;
import com.netty.trpc.registrycenter.provider.api.ProviderRegistryCenterRepository;
import com.netty.trpc.registrycenter.provider.nacos.NacosProviderRegistryCenterRepository;
import com.netty.trpc.registrycenter.provider.zookeeper.ZookeeperProviderRegistryCenterRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:01
 */
public class ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
    ProviderRegistryCenterRepository registryCenter;

    public ServiceRegistry(String serverList){
        registryCenter = new NacosProviderRegistryCenterRepository();
        registryCenter.init(new RegistryCenterMetadata(serverList));
    }

    public void registryService(String host, int port, Map<String,Object> serviceMap){
        Set<String> keySet = serviceMap.keySet();
        List<RpcServiceMetaInfo> serviceInfoList = new LinkedList<>();
        for (String key:keySet){
            String[] serviceInfo = key.split(ServiceUtil.service_connect_token);
            if (serviceInfo.length>0){
                String version=serviceInfo.length==2?serviceInfo[1]:"";
                serviceInfoList.add(new RpcServiceMetaInfo(serviceInfo[0],version));
            }else {
                LOGGER.warn("Can not get service name and version,{}",key);
            }
        }


        RegistryMetadata rpcProtocol = new RegistryMetadata(host, port);
        rpcProtocol.setServiceInfoList(serviceInfoList);

        registryCenter.registry(rpcProtocol);

    }

    public void unregistryService() {
        registryCenter.unregistry(null);
    }
}
