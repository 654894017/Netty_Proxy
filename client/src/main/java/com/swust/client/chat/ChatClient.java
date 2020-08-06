package com.swust.client.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.UUID;

/**
 * @author : MrLawrenc
 * date  2020/7/13 23:56
 */
public class ChatClient {

    public static void main(String[] args) {
        connect();
    }

    public static void connect() throws RuntimeException {
        try {
            final AttributeKey<String> id = AttributeKey.newInstance("ID");
            Bootstrap b = new Bootstrap();
            b.group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            System.out.println("channel id-->" + ctx.channel().attr(id));
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

                        }
                    });
            //绑定全局id和channel,这使得channel自带了一个全局唯一的channel id
            b.attr(id, UUID.randomUUID().toString() + "#" + System.currentTimeMillis());

            ChannelFuture future = b.connect("localhost", 10009).sync();
            future.addListener((ChannelFutureListener) future1 -> {
                boolean success = future1.isSuccess();
                System.out.println(success);
            });
        } catch (Exception e) {
            throw new RuntimeException("start client fail!", e);
        }
    }
}