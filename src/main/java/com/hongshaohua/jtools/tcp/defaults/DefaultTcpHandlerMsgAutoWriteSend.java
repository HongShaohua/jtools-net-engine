package com.hongshaohua.jtools.tcp.defaults;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Aska on 2018/2/6.
 */
public class DefaultTcpHandlerMsgAutoWriteSend<T extends DefaultTcpMsg> extends DefaultTcpHandlerMsgAutoWrite<T> {

    public DefaultTcpHandlerMsgAutoWriteSend(int id) {
        super(id);
    }

    @Override
    public void received(ChannelHandlerContext ctx, T msg) throws Exception {

    }
}
