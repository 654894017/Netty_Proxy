package com.swust.server.chat;

import com.swust.common.protocol.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author hz20035009-逍遥
 * date   2020/7/14 18:07
 * <p>
 * 服务端逻辑处理handler
 */
@Slf4j
@ChannelHandler.Sharable
public class ChatChannelHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            Message chatMsg = (Message) msg;
            String strMsg = new String(chatMsg.getData(), StandardCharsets.UTF_8);
        } else {
            System.out.println("=====》" + msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}