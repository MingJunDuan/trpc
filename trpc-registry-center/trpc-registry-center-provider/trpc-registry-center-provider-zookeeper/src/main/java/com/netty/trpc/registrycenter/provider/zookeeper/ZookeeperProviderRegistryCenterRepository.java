package com.netty.trpc.registrycenter.provider.zookeeper;

import com.alibaba.fastjson.JSONObject;
import com.netty.trpc.common.constant.TrpcConstant;
import com.netty.trpc.common.zookeeper.CuratorClient;
import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.provider.api.ProviderRegistryCenterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.spi.ServiceRegistry;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mjduan
 * @version 1.0
 * @date 2021-12-01 12:31
 */
public class ZookeeperProviderRegistryCenterRepository implements ProviderRegistryCenterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
    private CuratorClient client;
    private List<String> pathList = new LinkedList<>();

    @Override
    public void init(RegistryCenterMetadata metadata) {
        this.client = new CuratorClient(metadata.getServerList());
    }

    @Override
    public void registry(RegistryMetadata metadata) {
        byte[] bytes = JSONObject.toJSONString(metadata).getBytes(StandardCharsets.UTF_8);
        String path = TrpcConstant.ZK_DATA_PATH + "/" + metadata.getHost()+":"+metadata.getPort();
        try {
            client.createPathData(path,bytes);
            this.pathList.add(path);
            LOGGER.info("Registry new service, host:{}, port:{}, interfaceName:{}",metadata.getHost(),metadata.getPort(),metadata.getServiceInfoList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    @Override
    public void unregistry(RegistryMetadata metadata) {
        //just close and zk will remove ephemeral data
        client.close();
    }
}
