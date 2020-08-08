package com.swust.server.chat;

import com.swust.common.Starter;
import com.swust.common.codec.MessageDecoder0;
import com.swust.common.codec.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author : MrLawrenc
 * date  2020/7/13 23:57
 * <p>
 * 启动聊天服务器
 */
@Slf4j
public class ChatStarter implements Starter {
    @Override
    public boolean start(String[] args) throws Exception {
        int port = 10009;
        start0(port);
        return true;
    }

    private void start0(int port) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();

        ChatChannelHandler chatChannelHandler = new ChatChannelHandler();
        bootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new MessageDecoder0(), new MessageEncoder())
                                .addLast(new IdleStateHandler(60, 30, 0, TimeUnit.SECONDS))
                                .addLast("chatChannelHandler", chatChannelHandler);
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);


        bootstrap.bind(port).sync().addListener(fu -> {
            if (fu.isSuccess()) {
                log.info("Chat server start success!");
            } else {
                log.error("Chat server start fail! will close current service!");
                System.exit(0);
            }
        });
    }
}