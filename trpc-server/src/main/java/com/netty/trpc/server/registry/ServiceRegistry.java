package com.netty.trpc.server.registry;

import com.alibaba.fastjson.JSONObject;
import com.netty.trpc.common.constant.TrpcConstant;
import com.netty.trpc.common.protocol.RpcProtocol;
import com.netty.trpc.common.protocol.RpcServiceInfo;
import com.netty.trpc.common.util.ServiceUtil;
import com.netty.trpc.common.zookeeper.CuratorClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:01
 */
public class ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
    private CuratorClient client;
    private List<String> pathList = new LinkedList<>();

    public ServiceRegistry(String registryAddress){
        this.client = new CuratorClient(registryAddress);
    }

    public void registryService(String host, int port, Map<String,Object> serviceMap){
        Set<String> keySet = serviceMap.keySet();
        List<RpcServiceInfo> serviceInfoList = new LinkedList<>();
        for (String key:keySet){
            String[] serviceInfo = key.split(ServiceUtil.service_connect_token);
            if (serviceInfo.length>0){
                String version=serviceInfo.length==2?serviceInfo[1]:"";
                serviceInfoList.add(new RpcServiceInfo(serviceInfo[0],version));
            }else {
                LOGGER.warn("Can not get service name and version,{}",key);
            }
        }
        RpcProtocol rpcProtocol = new RpcProtocol(host, port);
        rpcProtocol.setServiceInfoList(serviceInfoList);
        byte[] bytes = JSONObject.toJSONString(rpcProtocol).getBytes(StandardCharsets.UTF_8);
        String path = TrpcConstant.ZK_DATA_PATH + "-" + rpcProtocol.hashCode();
        try {
            client.createPathData(path,bytes);
            this.pathList.add(path);
            LOGGER.info("Registry new service, host:{}, port:{}, interfaceName:{}",host,path,serviceInfoList);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }

        client.addConnectionStateListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                if (newState==ConnectionState.RECONNECTED){
                    registryService(host,port,serviceMap);
                }
            }
        });
    }

    public void unregistryService(){
        for (String s : pathList) {
            try {
                client.deletePath(s);
            }catch (Exception e){
                LOGGER.error(e.getMessage(),e);
            }
        }
        this.client.close();
    }
}
