package com.swust.server.handler;

import com.swust.server.ServerMain;
import com.swust.server.handler.http.HttpHelloWorldServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.util.ByteProcessor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author : MrLawrenc
 * date  2020/9/29 23:16
 * 协议分发器
 * <pre>
 *     1. 普通http服务
 *     2. 代理转发服务(包含所有基于tcp的服务)
 * </pre>
 */
@Slf4j
@ChannelHandler.Sharable
public class InboundSelectorHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String a = "GET /sadsdadsa HTTP/1.1\n" +
                "Host: localhost:9000\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\n" +
                "Sec-Fetch-Site: none\n" +
                "Sec-Fetch-Mode: navigate\n" +
                "Sec-Fetch-User: ?1\n" +
                "Sec-Fetch-Dest: document\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Accept-Language: zh-CN,zh;q=0.9\n" +
                "Cookie: Idea-2aaff583=58020cca-24ec-43dc-9508-48016715f7ae; _ga=GA1.1.465830549.1575734943";
        ChannelPipeline pipeline = ctx.pipeline();

        ByteBuf data = (ByteBuf) msg;
        //匹配第一个换行符(\n)
        int firstLf = data.forEachByte(ByteProcessor.FIND_LF);
        if (-1 != firstLf) {
            data.markReaderIndex();
            String firstLine = data.readCharSequence(firstLf, StandardCharsets.UTF_8).toString();
            data.resetReaderIndex();
            if (firstLine.contains("HTTP")) {
                log.info("this is http request,first line-->{}", firstLine);
                //走http流程

                pipeline.addLast(new HttpServerCodec())
                        .addLast(new HttpServerExpectContinueHandler())
                        .addLast(new HttpHelloWorldServerHandler());
                ctx.fireChannelRead(msg);
                return;
            }
        }

        //将server恢复到默认的代理服务
        ServerMain.addDefaultProxyHandler(pipeline);

        //continue execute
        ctx.fireChannelRead(msg);
    }

}