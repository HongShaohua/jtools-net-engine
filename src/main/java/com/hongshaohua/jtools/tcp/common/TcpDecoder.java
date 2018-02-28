package com.hongshaohua.jtools.tcp.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Aska on 2018/1/31.
 */
public class TcpDecoder extends ByteToMessageDecoder {

    private TcpHandler handler;

    public TcpDecoder(TcpHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while(true) {
            //分包
            ByteBuf buf = this.handler.unpack(ctx, in);
            if(buf == null) {
                break;
            }
            //解码
            Object msg = this.handler.decode(ctx, buf);
            if(msg == null) {
                break;
            }
            out.add(msg);
        }
    }
}
