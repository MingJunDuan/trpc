package com.netty.trpc.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 12:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcServiceInfo implements Serializable {
    private String serviceName;
    private String version;
}
