package com.netty.trpc.test.server;

import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.filter.TrpcFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 16:23
 */
@Service
public class ServerLogTrpcFilter implements TrpcFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLogTrpcFilter.class);

    @Override
    public boolean preFilter(TrpcRequest request) {
        LOGGER.info("Server receive request:{}",request);
        return true;
    }

    @Override
    public TrpcResponse postFilter(TrpcRequest request, TrpcResponse response, Throwable throwable) {
        LOGGER.info("Server return response:{}, it's request is: {}",response,request);
        return response;
    }
}
