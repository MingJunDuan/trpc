package com.netty.trpc.client.route;

import com.netty.trpc.client.handler.TrpcClientHandler;
import com.netty.trpc.common.protocol.RpcProtocol;
import com.netty.trpc.common.protocol.RpcServiceInfo;
import com.netty.trpc.common.util.ServiceUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 12:35
 */
public abstract class TrpcLoadBalance {

    protected Map<String, List<RpcProtocol>> getServiceMap(Map<RpcProtocol,TrpcClientHandler> connectedServerNodes){
        Map<String,List<RpcProtocol>> serviceMap=new HashMap<>();
        if (connectedServerNodes!=null&&connectedServerNodes.size()>0){
            for (RpcProtocol rpcProtocol : connectedServerNodes.keySet()) {
                for (RpcServiceInfo rpcServiceInfo : rpcProtocol.getServiceInfoList()) {
                    String serviceKey = ServiceUtil.serviceKey(rpcServiceInfo.getServiceName(), rpcServiceInfo.getVersion());
                    List<RpcProtocol> rpcProtocolList = serviceMap.get(serviceKey);
                    if (rpcProtocolList==null){
                        rpcProtocolList=new LinkedList<>();
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
