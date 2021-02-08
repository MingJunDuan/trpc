package com.netty.trpc.server.core;

import com.netty.trpc.codec.PingPongRequest;
import com.netty.trpc.codec.TrpcRequest;
import com.netty.trpc.codec.TrpcResponse;
import com.netty.trpc.log.LOG;
import com.netty.trpc.util.ServiceUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-08 14:42
 */
public class TrpcServerHandler extends SimpleChannelInboundHandler<TrpcRequest> {
    private Map<String,Object> handlerMap;
    private ThreadPoolExecutor handlerPool;

    public TrpcServerHandler(Map<String,Object> handlerMap,ThreadPoolExecutor executor){
        this.handlerMap = handlerMap;
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
                LOG.info("Service request:{}",trpcRequest.getRequestId());
                TrpcResponse response = new TrpcResponse();
                response.setRequestId(trpcRequest.getRequestId());
                try {
                    Object result = handle(trpcRequest);
                    response.setResult(result);
                }catch (Exception e){
                    response.setError(e.getMessage());
                    LOG.error(e);
                }
                context.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        LOG.info("Send response for request:{}",response.getRequestId());
                    }
                });
            }
        });
    }

    private Object handle(TrpcRequest trpcRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String className = trpcRequest.getClassName();
        String version = trpcRequest.getVersion();
        String serviceKey = ServiceUtil.serviceKey(className, version);
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean==null){
            String msg="Can not find service implement with interface name: "+className+" and version: "+version;
            LOG.error(msg);
            throw new IllegalStateException(msg);
        }

        Class<?> beanClass = serviceBean.getClass();
        String methodName = trpcRequest.getMethodName();
        Class<?>[] parameterTypes = trpcRequest.getParameterTypes();
        Object[] parameters = trpcRequest.getParameters();
        Method method = beanClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean,parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.warn("server caught exception: {}",cause.getMessage());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            ctx.channel().closeFuture();
            LOG.warn("Channel idle in last {} seconds, close it",PingPongRequest.BEAT_TIMEOUT);
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
