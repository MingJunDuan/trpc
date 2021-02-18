package com.netty.trpc.test;

import com.alibaba.fastjson.JSONObject;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.common.protocol.RpcProtocol;
import org.junit.Test;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 15:01
 */
public class JsonTest {

    @Test
    public void test(){
        String str="{\"host\":\"127.0.0.1\",\"port\":18878,\"serviceInfoList\":[{\"serviceName\":\"com.netty.trpc.test.service.IPersonService\",\"version\":\"\"},{\"serviceName\":\"com.netty.trpc.test.service.IHelloService\",\"version\":\"\"}]}";
        RpcProtocol rpcProtocol = JSONObject.parseObject(str, RpcProtocol.class);
        LOG.info(rpcProtocol);
    }
}
