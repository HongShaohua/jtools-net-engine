package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.tcp.server.defaults.DefaultTcpHandlerMapListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Aska on 2018/2/7.
 */
public class MyListener extends DefaultTcpHandlerMapListener {

    @Override
    public void connect(ChannelHandlerContext ctx) throws Exception {
        super.connect(ctx);
        System.out.println();
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx) throws Exception {
        super.disconnect(ctx);
        System.out.println();
    }
}
