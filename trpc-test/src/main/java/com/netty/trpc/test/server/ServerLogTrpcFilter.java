package com.netty.trpc.test.server;

import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.filter.TrpcFilter;
import com.netty.trpc.common.log.LOG;
import org.springframework.stereotype.Service;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 16:23
 */
@Service
public class ServerLogTrpcFilter implements TrpcFilter {

    @Override
    public boolean preFilter(TrpcRequest request) {
        LOG.info("Server receive request:{}",request);
        return true;
    }

    @Override
    public TrpcResponse postFilter(TrpcRequest request, TrpcResponse response, Throwable throwable) {
        LOG.info("Server return response:{}, it's request is: {}",response,request);
        return response;
    }
}
