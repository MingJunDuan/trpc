package com.netty.trpc.registrycenter.common;

import lombok.Data;

/**
 * @author mjduan
 * @version 1.0
 * @date 2021-11-30 20:50
 */
@Data
public class RpcServiceMetaInfo {
    private String serviceName;
    private String version;
}
