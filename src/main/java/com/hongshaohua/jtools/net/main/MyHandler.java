package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.tcp.server.defaults.DefaultTcpHandlerMsgAutoWriteRecv;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Aska on 2018/2/6.
 */
public class MyHandler extends DefaultTcpHandlerMsgAutoWriteRecv<MyMsg> {

    public MyHandler(int id) {
        super(id);
    }

    @Override
    public void received(ChannelHandlerContext ctx, MyMsg msg) throws Exception {
        System.out.println();
    }
}
