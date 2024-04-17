package com.netty.trpc.config.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.netty.trpc.config.api.TrpcConfigApi;

public class ApolloTrpcConfig implements TrpcConfigApi {
    private static final String DEFAULT_NAMESPACE = "trpc_config";
    private final String namespace;
    private final Config apolloConfig;

    public ApolloTrpcConfig(String namespace){
        this.namespace = StringUtils.isBlank(namespace)?DEFAULT_NAMESPACE:namespace;
        this.apolloConfig = ConfigService.getConfig(namespace);
    }

    @Override
    public String getInternalProperty(String key) {
        String value = apolloConfig.getProperty(key, null);
        return value;
    }
}
