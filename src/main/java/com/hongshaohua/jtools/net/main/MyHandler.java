package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.tcp.server.TcpServerDecoder;
import com.hongshaohua.jtools.tcp.server.TcpServerEncoder;
import com.hongshaohua.jtools.tcp.server.defaults.DefaultTcpHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Aska on 2018/2/5.
 */
public class MyHandler extends DefaultTcpHandler<ByteBuf> {

    public MyHandler() {
    }

    @Override
    public TcpServerDecoder newDecoder() {
        return new TcpServerDecoder(this);
    }

    @Override
    public TcpServerEncoder<ByteBuf> newEncoder() {
        return new MyEncoder(this);
    }

    @Override
    protected ByteBuf deserialize(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        return buf;
    }

    @Override
    protected ByteBuf serialize(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        return msg;
    }

    @Override
    public void received(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println();
    }
}
