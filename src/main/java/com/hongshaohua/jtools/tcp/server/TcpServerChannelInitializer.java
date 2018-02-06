package com.hongshaohua.jtools.tcp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Aska on 2018/1/31.
 */
public class TcpServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private TcpServerInitializer initializer;

    public TcpServerChannelInitializer(TcpServerInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //每次有新连接都会触发init
        ch.pipeline().addLast(this.initializer.decoder());
        ch.pipeline().addLast(this.initializer.encoder());
        ch.pipeline().addLast(new TcpServerInboundHandler(this.initializer.handler()));
    }
}
