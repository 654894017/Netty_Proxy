package com.swust.server.handler;

import com.swust.common.codec.MessageDecoder1;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.ByteProcessor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author hz20035009-逍遥
 * date   2020/7/28 17:56
 * <p>
 * 消息分发器
 */
@ChannelHandler.Sharable
@Slf4j
public class MessageDispatcher extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    /**
     * http协议如下:
     * <pre>
     *
     * </pre>
     */
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
        ByteBuf data = (ByteBuf) msg;

        //第一个换行符(\n)
        int firstLf = data.forEachByte(ByteProcessor.FIND_LF);
        if (-1 != firstLf) {
            data.markReaderIndex();
            String firstLine = data.readCharSequence(firstLf, StandardCharsets.UTF_8).toString();
            data.resetReaderIndex();
            if (firstLine.contains("HTTP")) {
                log.info("this is http request,first line-->{}", firstLine);
            }
        }


        if (false) {
            int len = data.getInt(4);
            data.getBytes(4, new byte[1024]);

            ChannelPipeline pipeline = ctx.pipeline();
            //动态更改handler
            pipeline.replace("encode", "encode", new MessageDecoder1());
            pipeline.replace("decode", "decode", new MessageDecoder1());
            pipeline.replace("businessHandler", "businessHandler", new MessageDecoder1());
        }

        //continue execute
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}