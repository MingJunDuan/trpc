package com.netty.trpc.client.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.common.util.ServiceUtil;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 12:35
 */
public abstract class TrpcLoadBalance {

    protected Map<String, List<RegistryMetadata>> getServiceMap(Map<RegistryMetadata,TrpcClientHandler> connectedServerNodes){
        Map<String, List<RegistryMetadata>> serviceMap = new HashMap<>(32);
        if (connectedServerNodes!=null&&connectedServerNodes.size()>0){
            Set<RegistryMetadata> rpcProtocolsKeySet = connectedServerNodes.keySet();
            for (RegistryMetadata rpcProtocol : rpcProtocolsKeySet) {
                for (RpcServiceMetaInfo rpcServiceInfo : rpcProtocol.getServiceInfoList()) {
                    String serviceKey = ServiceUtil.serviceKey(rpcServiceInfo.getServiceName(), rpcServiceInfo.getVersion());
                    List<RegistryMetadata> rpcProtocolList = serviceMap.get(serviceKey);
                    if (rpcProtocolList==null){
                        rpcProtocolList = new ArrayList<>(rpcProtocolsKeySet.size());
                    }
                    rpcProtocolList.add(rpcProtocol);
                    serviceMap.putIfAbsent(serviceKey,rpcProtocolList);
                }
            }
        }
        return serviceMap;
    }


    public abstract RegistryMetadata route(String serviceKey, Map<RegistryMetadata, TrpcClientHandler> connectedServerNodes) throws Exception;

    public List<RegistryMetadata> routes(String serviceKey, Map<RegistryMetadata, TrpcClientHandler> connectedServerNodes) throws Exception{
        return Collections.emptyList();
    }
}
