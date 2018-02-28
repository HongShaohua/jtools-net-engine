package com.hongshaohua.jtools.tcp.client;

import com.hongshaohua.jtools.tcp.common.TcpChannelInitializer;
import com.hongshaohua.jtools.tcp.common.TcpInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Aska on 2018/2/11.
 */
public class TcpClient {

    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;

    public TcpClient(int worker) {
        if(worker <= 0) {
            this.workerGroup = new NioEventLoopGroup();
        } else {
            this.workerGroup = new NioEventLoopGroup(worker);
        }
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.workerGroup);
        this.bootstrap.channel(NioSocketChannel.class);
        this.bootstrap.option(ChannelOption.SO_BACKLOG, 2048);
        this.bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        this.bootstrap.option(ChannelOption.TCP_NODELAY, true);
    }

    public TcpClient() {
        this(0);
    }

    public ChannelFuture connect(String host, int port, TcpInitializer initializer) {
        this.bootstrap.handler(new TcpChannelInitializer(initializer));
        return this.bootstrap.connect(host, port);
    }
}
