package com.netty.trpc.common.serializer.hessian;

import com.netty.trpc.common.protocol.RpcProtocol;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-19 10:38
 */
public class Hessian2SerializerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hessian2SerializerTest.class);

    @Test
    public void serialize() {
        RpcProtocol rpcProtocol = new RpcProtocol();
        rpcProtocol.setHost("localhost");
        rpcProtocol.setPort(8080);
        rpcProtocol.setServiceInfoList(new LinkedList<>());

        Hessian2Serializer serializer = new Hessian2Serializer();
        byte[] data = serializer.serialize(rpcProtocol);
        RpcProtocol result = serializer.deserialize(data, RpcProtocol.class);
        LOGGER.info(result.toString());
    }

}