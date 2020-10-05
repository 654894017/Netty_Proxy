package com.swust.server.handler;

import com.swust.common.protocol.Message;
import com.swust.common.protocol.ProtocolType;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hz20035009-逍遥
 * date   2020/7/28 17:56
 * <p>
 * 消息分发器，分发{@link Message}类型的消息
 */
@ChannelHandler.Sharable
@Slf4j
public class MessageDispatcher extends SimpleChannelInboundHandler<Message> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        ProtocolType protocolType = msg.getHeader().getProtocolType();
        switch (protocolType) {
            case PROXY:
                log.info("current req is proxy");
            case FILE_SYNC:
                log.info("current req is file sync");
            default:
                ctx.fireChannelRead(msg);
        }
    }
}