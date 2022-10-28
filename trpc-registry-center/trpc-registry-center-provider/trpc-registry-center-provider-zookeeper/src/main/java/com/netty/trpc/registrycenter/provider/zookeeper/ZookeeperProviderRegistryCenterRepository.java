package com.netty.trpc.registrycenter.provider.zookeeper;

import com.netty.trpc.common.constant.TrpcConstant;
import com.netty.trpc.common.extension.SPI;
import com.netty.trpc.common.zookeeper.CuratorClient;
import com.netty.trpc.registrycenter.common.RegistryCenterMetadata;
import com.netty.trpc.registrycenter.common.RegistryMetadata;
import com.netty.trpc.registrycenter.common.RpcServiceMetaInfo;
import com.netty.trpc.registrycenter.provider.api.ProviderRegistryCenterRepository;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.spi.ServiceRegistry;
import java.util.List;

/**
 * @author mjduan
 * @version 1.0
 * @date 2021-12-01 12:31
 */
@SPI(name = "zookeeper")
public class ZookeeperProviderRegistryCenterRepository implements ProviderRegistryCenterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
    private CuratorClient client;

    @Override
    public void init(RegistryCenterMetadata metadata) {
        this.client = new CuratorClient(metadata.getServerList());
    }

    @Override
    public void registry(RegistryMetadata metadata) {
        List<RpcServiceMetaInfo> serviceInfoList = metadata.getServiceInfoList();
        for (RpcServiceMetaInfo rpcServiceMetaInfo : serviceInfoList) {
            String serviceName = rpcServiceMetaInfo.getServiceName() + ":" + rpcServiceMetaInfo.getVersion();
            String providerNode = TrpcConstant.ZK_DATA_PATH + "/" + serviceName + "/" + TrpcConstant.ZK_DATA_PROVIDER;
            String node = metadata.getHost() + ":" + metadata.getPort();
            try {
                if (!client.pathExist(providerNode)) {
                    client.createPathData(providerNode, null,CreateMode.PERSISTENT);
                }
                client.createPathData(providerNode + "/" + node, null);
                LOGGER.info("Registry new service, host:{}, port:{}, interfaceName:{}", metadata.getHost(), metadata.getPort(), metadata.getServiceInfoList());
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void unregistry(RegistryMetadata metadata) {
        //just close and zk will remove ephemeral data
        client.close();
    }
}
