package com.netty.trpc.client.discovery;

import com.alibaba.fastjson.JSONObject;
import com.netty.trpc.common.constant.TrpcConstant;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.common.protocol.RpcProtocol;
import com.netty.trpc.common.zookeeper.CuratorClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 10:31
 */
public class ServiceDiscovery {
    private CuratorClient curatorClient;

    public ServiceDiscovery(String registryAddress) {
        this.curatorClient = new CuratorClient(registryAddress);
        discoveryService();
    }

    private void discoveryService() {
        try {
            //Get initial service ingo
            LOG.info("Get initial service info");
            getServiceAndUpdateServer();
            //Add watch listener
            curatorClient.watchPathChildrenNode(TrpcConstant.ZK_REGISTRY_PATH, new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    PathChildrenCacheEvent.Type type = pathChildrenCacheEvent.getType();
                    switch (type){
                        case CONNECTION_RECONNECTED:
                            LOG.info("Reconnected to zk, try to get latest service list");
                            getServiceAndUpdateServer();
                            break;
                        case CHILD_ADDED:
                        case CHILD_UPDATED:
                        case CHILD_REMOVED:
                            LOG.info("Service info changed, try to get latest service list");
                            getServiceAndUpdateServer();
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getServiceAndUpdateServer() {
        try {
            List<String> nodeList = curatorClient.getChildren(TrpcConstant.ZK_REGISTRY_PATH);
            List<RpcProtocol> rpcProtocols = new ArrayList<>(nodeList.size());
            for (String node : nodeList) {
                byte[] data = curatorClient.getData(TrpcConstant.ZK_REGISTRY_PATH + "/" + node);
                String str = new String(data, StandardCharsets.UTF_8);
                RpcProtocol rpcProtocol = JSONObject.parseObject(str, RpcProtocol.class);
                rpcProtocols.add(rpcProtocol);
            }
            LOG.info("Service node data: {}", rpcProtocols);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateConnectedServer(List<RpcProtocol> rpcProtocols) {

    }

    public void stop() {
        this.curatorClient.close();
    }
}
