package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.tcp.defaults.DefaultTcpHandlerMsgAutoWriteRecv;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Aska on 2018/2/6.
 */
public class MyHandler extends DefaultTcpHandlerMsgAutoWriteRecv<MyMsg> {

    private String name;

    public MyHandler(int id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public void connect(ChannelHandlerContext ctx) throws Exception {
        super.connect(ctx);
    }

    @Override
    public void received(ChannelHandlerContext ctx, MyMsg msg) throws Exception {
        System.out.println();
    }
}
