package com.hongshaohua.jtools.tcp.server.defaults;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Aska on 2018/2/2.
 */
public abstract class DefaultTcpHandlerMsgRecv<T extends DefaultTcpMsg> extends DefaultTcpHandlerMsg<T> {

    public DefaultTcpHandlerMsgRecv(int id) {
        super(id);
    }

    @Override
    protected ByteBuf serialize(ChannelHandlerContext ctx, T msg) throws Exception {
        return null;
    }
}
