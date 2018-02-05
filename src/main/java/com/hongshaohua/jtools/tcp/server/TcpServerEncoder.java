package com.hongshaohua.jtools.tcp.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Aska on 2018/1/31.
 */
public class TcpServerEncoder<T> extends MessageToByteEncoder<T> {

    private TcpServerHandler handler;

    public TcpServerEncoder(TcpServerHandler handler) {
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception {
        //编码
        ByteBuf buf = this.handler.encode(ctx, msg);
        if(buf != null) {
            this.write(buf, out);
        }
    }

    private synchronized void write(ByteBuf buf, ByteBuf out) throws Exception {
        out.writeBytes(buf);
    }
}
