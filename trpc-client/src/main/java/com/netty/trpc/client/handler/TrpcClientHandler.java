package com.netty.trpc.client.handler;

import com.netty.trpc.client.connect.ConnectionManagerFactory;
import com.netty.trpc.common.codec.PingPongRequest;
import com.netty.trpc.common.codec.TrpcRequest;
import com.netty.trpc.common.codec.TrpcResponse;
import com.netty.trpc.common.protocol.RpcProtocol;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-02-18 11:24
 */
public class TrpcClientHandler extends SimpleChannelInboundHandler<TrpcResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrpcClientHandler.class);
    private ConcurrentHashMap<String,TrpcFuture> pendingRpc = new ConcurrentHashMap<>();
    private volatile Channel channel;
    private SocketAddress remotePeer;
    private RpcProtocol rpcProtocol;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TrpcResponse response) throws Exception {
        String requestId = response.getRequestId();
        TrpcFuture trpcFuture = pendingRpc.get(requestId);
        if (trpcFuture!=null){
            pendingRpc.remove(requestId);
            trpcFuture.done(response);
        }else {
            LOGGER.error("Can not get pending response for requestId: {}",requestId);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Client caught exception: {}",cause.getMessage());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            sendRequest(PingPongRequest.BEAT_PING_REQUEST);
            LOGGER.info("Client send beat ping-pong to {}",remotePeer);
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }

    public TrpcFuture sendRequest(TrpcRequest request){
        TrpcFuture trpcFuture = new TrpcFuture(request);
        pendingRpc.put(request.getRequestId(),trpcFuture);
        try{
            ChannelFuture channelFuture = channel.writeAndFlush(request).sync();
            if (!channelFuture.isSuccess()){
                LOGGER.error("Send request {} error",request.getRequestId());
            }
        } catch (InterruptedException e) {
            LOGGER.error("Send request exception:{}",e.getMessage());
        }
        return trpcFuture;
    }

    public void close(){
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public void setRpcProtocol(RpcProtocol rpcProtocol) {
        this.rpcProtocol = rpcProtocol;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ConnectionManagerFactory.getConnectionManager().removeHandler(rpcProtocol);
    }
}
