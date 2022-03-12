package com.netty.trpc.server.core;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.netty.trpc.common.codec.PingPongRequest;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.filter.TrpcFilter;
import com.netty.trpc.common.util.MethodUtils;
import com.netty.trpc.common.util.ServiceUtil;
import com.netty.trpc.common.util.SystemClock;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:42
 */
public class TrpcServerHandler extends SimpleChannelInboundHandler<TrpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcServerHandler.class);
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
            LOGGER.info("Server read heartbeat ping pong");
            return;
        }
        handlerPool.execute(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("Service request:{}", trpcRequest.getRequestId());
                long startTime = SystemClock.currentTimeMillis();
                TrpcResponse response = new TrpcResponse();
                response.setRequestId(trpcRequest.getRequestId());
                Exception ex = null;
                if (applyPreFilter(trpcRequest)) {
                    try {
                        Object result = doHandle(trpcRequest);
                        response.setResult(result);
                    } catch (Exception e) {
                        String errorMsg = ((InvocationTargetException) e).getTargetException().getMessage();
                        response.setError(errorMsg);
                        ex = e;
                        LOGGER.error(e.getMessage(), e);
                    }
                    response = applyPostFilter(trpcRequest, response, ex);
                }

                if (context.channel().isActive() && context.channel().isWritable()) {
                    CustomChannelFutureListener listener = new CustomChannelFutureListener(trpcRequest, response, ex, startTime);
                    context.channel().writeAndFlush(response).addListener(listener);
                } else {
                    LOGGER.error("Message dropped because channel is unwritable!");
                }
            }
        });
    }

    private boolean applyPreFilter(TrpcRequest trpcRequest) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }
        for (int i = 0; i < filters.size(); i++) {
            boolean canPost = filters.get(i).preFilter(trpcRequest);
            if (!canPost) {
                LOGGER.info("Filter return false, return directly");
                return false;
            }
        }
        return true;
    }

    private TrpcResponse applyPostFilter(TrpcRequest trpcRequest, TrpcResponse response, Throwable throwable) {
        if (filters == null || filters.isEmpty()) {
            return response;
        }
        TrpcResponse res = response;
        for (int i = filters.size() - 1; i >= 0; i--) {
            res = filters.get(i).postFilter(trpcRequest, res, throwable);
        }
        return res;
    }

    private Object doHandle(TrpcRequest trpcRequest) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, ClassNotFoundException {
        String className = trpcRequest.getInterfaceName();
        String version = trpcRequest.getVersion();
        String serviceKey = ServiceUtil.serviceKey(className, version);
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean == null) {
            String msg = "Can not find service implement with interface name: " + className + " and version: " + version;
            LOGGER.error(msg);
            throw new IllegalStateException(msg);
        }

        Class<?> beanClass = serviceBean.getClass();
        String methodName = trpcRequest.getMethodName();
        Class<?>[] parameterTypes = getParameterTypes(trpcRequest);
        Object[] parameters = trpcRequest.getParameters();
        return MethodUtils.invokeMethod(serviceBean, beanClass, methodName, parameterTypes, parameters);
    }

    private Class<?>[] getParameterTypes(TrpcRequest trpcRequest) throws ClassNotFoundException {
        if (trpcRequest.isGeneric()) {
            String[] list = trpcRequest.getParameterTypeStrList();
            ClassLoader classLoader = trpcRequest.getClass().getClassLoader();
            Class<?>[] parameterTypes = new Class[list.length];
            for (int i = 0; i < list.length; i++) {
                parameterTypes[i] = classLoader.loadClass(list[i]);
            }
            return parameterTypes;
        }
        return trpcRequest.getParameterTypes();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.warn("server caught exception: {}", cause.getMessage(), cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().closeFuture();
            LOGGER.warn("Channel idle in last {} seconds, close it", PingPongRequest.BEAT_TIMEOUT);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
