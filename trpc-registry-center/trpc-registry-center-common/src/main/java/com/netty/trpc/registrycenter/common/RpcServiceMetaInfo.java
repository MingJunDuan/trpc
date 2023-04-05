package com.netty.trpc.registrycenter.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author mjduan
 * @version 1.0
 * @date 2021-11-30 20:50
 */
@Data
@NoArgsConstructor
public class RpcServiceMetaInfo {
    private String serviceName;
    private String version;

    private int warmUp = 100;
    private long uptime = 2000;

    public RpcServiceMetaInfo(String serviceName, String version) {
        this.serviceName = serviceName;
        this.version = version;
    }

    public RpcServiceMetaInfo(String serviceName, String version, int warmUp, long uptime) {
        this.serviceName = serviceName;
        this.version = version;
        this.warmUp = warmUp;
        this.uptime = uptime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcServiceMetaInfo that = (RpcServiceMetaInfo) o;
        return serviceName.equals(that.serviceName) &&
                version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, version);
    }
}
