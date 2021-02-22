package com.netty.trpc.client.route;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.common.protocol.RpcProtocol;
import com.netty.trpc.common.protocol.RpcServiceInfo;
import com.netty.trpc.common.util.ServiceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 12:35
 */
public abstract class TrpcLoadBalance {

    protected Map<String, List<RpcProtocol>> getServiceMap(Map<RpcProtocol,TrpcClientHandler> connectedServerNodes){
        Map<String, List<RpcProtocol>> serviceMap = new HashMap<>(32);
        if (connectedServerNodes!=null&&connectedServerNodes.size()>0){
            Set<RpcProtocol> rpcProtocolsKeySet = connectedServerNodes.keySet();
            for (RpcProtocol rpcProtocol : rpcProtocolsKeySet) {
                for (RpcServiceInfo rpcServiceInfo : rpcProtocol.getServiceInfoList()) {
                    String serviceKey = ServiceUtil.serviceKey(rpcServiceInfo.getServiceName(), rpcServiceInfo.getVersion());
                    List<RpcProtocol> rpcProtocolList = serviceMap.get(serviceKey);
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


    public abstract RpcProtocol route(String serviceKey, Map<RpcProtocol, TrpcClientHandler> connectedServerNodes) throws Exception;
}
