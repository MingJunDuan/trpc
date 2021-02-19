package com.netty.trpc.server.core;

import com.netty.trpc.common.codec.PingPongRequest;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.filter.TrpcFilter;
import com.netty.trpc.common.log.LOG;
import com.netty.trpc.common.util.ServiceUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:42
 */
public class TrpcServerHandler extends SimpleChannelInboundHandler<TrpcRequest> {
    private Map<String, Object> handlerMap;
    private List<TrpcFilter> filters;
    private ThreadPoolExecutor handlerPool;

    public TrpcServerHandler(Map<String, Object> handlerMap, List<TrpcFilter> filters, ThreadPoolExecutor executor) {
        this.handlerMap = handlerMap;
        this.filters = filters;
        this.handlerPool = executor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, TrpcRequest trpcRequest) throws Exception {
        if (trpcRequest.getRequestId().equalsIgnoreCase(PingPongRequest.BEAT_ID)) {
            LOG.info("Server read heartbeat ping pong");
            return;
        }
        handlerPool.execute(new Runnable() {
            @Override
            public void run() {
                LOG.info("Service request:{}", trpcRequest.getRequestId());
                TrpcResponse response = new TrpcResponse();
                response.setRequestId(trpcRequest.getRequestId());
                Exception ex = null;
                if (applyPreFilter(trpcRequest)){
                    try {
                        Object result = doHandle(trpcRequest);
                        response.setResult(result);
                    } catch (Exception e) {
                        response.setError(e.getMessage());
                        ex = e;
                        LOG.error(e);
                    }
                    response=applyPostFilter(trpcRequest, response, ex);
                }
                context.writeAndFlush(response).addListener(new CustomChannelFutureListener(trpcRequest,response,ex));
            }
        });
    }

    private boolean applyPreFilter(TrpcRequest trpcRequest) {
        if (filters==null||filters.isEmpty()){
            return true;
        }
        for (int i = 0; i < filters.size(); i++) {
            boolean canPost = filters.get(i).preFilter(trpcRequest);
            if (!canPost) {
                LOG.info("Filter return false, return directly");
                return false;
            }
        }
        return true;
    }

    private TrpcResponse applyPostFilter(TrpcRequest trpcRequest, TrpcResponse response, Throwable throwable) {
        if (filters==null||filters.isEmpty()){
            return response;
        }
        TrpcResponse res=response;
        for (int i = filters.size() - 1; i >= 0; i--) {
            res=filters.get(i).postFilter(trpcRequest, res, throwable);
        }
        return res;
    }

    private Object doHandle(TrpcRequest trpcRequest) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String className = trpcRequest.getInterfaceName();
        String version = trpcRequest.getVersion();
        String serviceKey = ServiceUtil.serviceKey(className, version);
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean == null) {
            String msg = "Can not find service implement with interface name: " + className + " and version: " + version;
            LOG.error(msg);
            throw new IllegalStateException(msg);
        }

        Class<?> beanClass = serviceBean.getClass();
        String methodName = trpcRequest.getMethodName();
        Class<?>[] parameterTypes = trpcRequest.getParameterTypes();
        Object[] parameters = trpcRequest.getParameters();
        Method method = beanClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.warn("server caught exception: {}", cause.getCause());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().closeFuture();
            LOG.warn("Channel idle in last {} seconds, close it", PingPongRequest.BEAT_TIMEOUT);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
