package com.netty.trpc.common.codec.customprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * https://www.jianshu.com/p/068f82d2cf2f
 *
 * @author DuanMingJun
 * @version 1.0
 * @date 2021-05-11 17:07
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<TrpcMessageProtocol> {
    private int count;

    //服务端接收到客户端的请求
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TrpcMessageProtocol msg) throws Exception {
        //接收到数据并处理
        int messageType = msg.getType();
        int length = msg.getLength();
        byte[] content = msg.getContent();
        System.out.println("服务器端接收到消息类型：" + messageType);
        System.out.println("服务器端接收到消息数据长度：" + length);
        System.out.println("服务器端接收到消息数据：" + new String(content, CharsetUtil.UTF_8));
        System.out.println("服务器端接收到消息数量："+(++count));

        //服务器会送数据给客户端，回送一个随机id值
        byte[] responseContent = UUID.randomUUID().toString().getBytes(CharsetUtil.UTF_8);
        TrpcMessageProtocol messageProtocol = new TrpcMessageProtocol();
        messageProtocol.setType(1);
        messageProtocol.setLength(responseContent.length);
        messageProtocol.setContent(responseContent);

        ctx.channel().writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
