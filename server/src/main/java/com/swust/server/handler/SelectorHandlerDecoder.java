package com.swust.server.handler;

import com.swust.common.codec.MessageDecoder1;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author : MrLawrenc
 * date  2020/9/29 23:16
 * 协议分发器
 */
@ChannelHandler.Sharable
public class SelectorHandlerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf data, List<Object> out) throws Exception {

        if (false) {
            int len = data.getInt(4);
            data.getBytes(4, new byte[1024]);

            ChannelPipeline pipeline = ctx.pipeline();
            //动态更改handler
            pipeline.replace("encode", "encode", new MessageDecoder1());
            pipeline.replace("decode", "decode", new MessageDecoder1());
            pipeline.replace("businessHandler", "businessHandler", new MessageDecoder1());
        }

        out.add(data);
        //continue execute
    }
}