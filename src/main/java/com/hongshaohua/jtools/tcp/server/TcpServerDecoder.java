package com.hongshaohua.jtools.tcp.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Aska on 2018/1/31.
 */
public class TcpServerDecoder extends ByteToMessageDecoder {

    private TcpServerHandler handler;

    public TcpServerDecoder(TcpServerHandler handler) {
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
