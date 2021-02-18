package com.netty.trpc.common.protocol;

import lombok.Data;

import java.util.List;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 12:58
 */
@Data
public class RpcProtocol {
    private String host;
    private int port;
    private List<RpcServiceInfo> serviceInfoList;

    public RpcProtocol(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
