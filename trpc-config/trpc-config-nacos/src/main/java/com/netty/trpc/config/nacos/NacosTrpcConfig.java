package com.netty.trpc.config.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.netty.trpc.config.api.TrpcConfigApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class NacosTrpcConfig implements TrpcConfigApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosTrpcConfig.class);
    private static final String DEFAULT_GROUP = "trpc";
    private static final long DEFAULT_TIMEOUT = 5000;
    private final String serverAdd;
    private ConfigService configService;

    public NacosTrpcConfig(String serverAddr) throws NacosException {
        this.serverAdd = serverAddr;
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, this.serverAdd);
        configService = NacosFactory.createConfigService(properties);
    }

    @Override
    public Object getInternalProperty(String key) {
        String content = null;
        try {
            content = configService.getConfig(key, DEFAULT_GROUP, DEFAULT_TIMEOUT);
        } catch (NacosException e) {
            LOGGER.error("Nacos get configuration error with key {}",key,e);
        }
        return content;
    }
}

