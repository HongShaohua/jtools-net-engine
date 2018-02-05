package com.hongshaohua.jtools.tcp.server.defaults;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Aska on 2018/2/2.
 */
public abstract class DefaultTcpHandlerMsgSend<T extends DefaultTcpMsg> extends DefaultTcpHandlerMsg<T> {

    public DefaultTcpHandlerMsgSend(int id) {
        super(id);
    }

    @Override
    protected T deserialize(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        return null;
    }

    @Override
    public void received(ChannelHandlerContext ctx, T msg) throws Exception {

    }
}
