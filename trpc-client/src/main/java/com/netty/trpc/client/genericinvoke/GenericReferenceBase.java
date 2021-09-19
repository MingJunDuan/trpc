/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：trpc
 * 注意：未经作者允许，不得外传
 */
package com.netty.trpc.client.genericinvoke;

import com.netty.trpc.client.TrpcClient;
import lombok.Data;

/**
 * @author dmj1161859184@126.com 2021-09-19 23:08
 * @version 1.0
 * @since 1.0
 */
@Data
public class GenericReferenceBase {
    protected TrpcClient trpcClient;
    protected String interfaceName;
    protected String version;
    protected String methodName;
    protected String[] parameterTypes;
}
