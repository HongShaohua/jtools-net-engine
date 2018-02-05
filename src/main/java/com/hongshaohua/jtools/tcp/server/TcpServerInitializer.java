package com.hongshaohua.jtools.tcp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Aska on 2018/1/31.
 */
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private TcpServerHandler handler;

    public TcpServerInitializer(TcpServerHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("run once TcpServerInitializer");
        ch.pipeline().addLast(this.handler.newDecoder());
        ch.pipeline().addLast(this.handler.newEncoder());
        ch.pipeline().addLast(new TcpServerInboundHandler(this.handler));
    }
}
