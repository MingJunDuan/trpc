package com.netty.trpc.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrpcNettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private ExecutorService executor = Executors.newCachedThreadPool(runnable -> {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName("NettyHttpHandler-" + thread.getName());
        return thread;
    });

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        executor.execute(() -> onReceivedRequest(ctx,request));
    }

    private void onReceivedRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        String str="Hello world";
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(str.getBytes(StandardCharsets.UTF_8)));
        response.headers().set("Content-Type", "text/html; charset=utf-8");
        response.headers().setInt("Content-Length", response.content().readableBytes());

//        boolean isKeepAlive = HttpUtil.isKeepAlive(response);
//        if (!isKeepAlive) {
//            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
//        } else {
//            response.headers().set("Connection", "keep-alive");
//            ctx.write(response);
//        }
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        ctx.flush();
        //ReferenceCountUtil.release(request);
    }
}
