package com.hongshaohua.jtools.tcp.server;

import com.hongshaohua.jtools.tcp.common.TcpChannelInitializer;
import com.hongshaohua.jtools.tcp.common.TcpInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ExecutorService;

/**
 * Created by Aska on 2018/1/30.
 */
public class TcpServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;
    private ExecutorService executor;

    public TcpServer(int boss, int worker, ExecutorService executor) {
        if(boss <= 0) {
            this.bossGroup = new NioEventLoopGroup();
        } else {
            this.bossGroup = new NioEventLoopGroup(boss);
        }
        if(worker <= 0) {
            this.workerGroup = new NioEventLoopGroup();
        } else {
            this.workerGroup = new NioEventLoopGroup(worker);
        }
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.group(this.bossGroup, this.workerGroup);
        this.serverBootstrap.channel(NioServerSocketChannel.class);
        this.serverBootstrap.option(ChannelOption.SO_BACKLOG, 2048);
        this.serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        this.serverBootstrap.option(ChannelOption.TCP_NODELAY, true);

        this.executor = executor;
    }

    public TcpServer(int boss, ExecutorService executor) {
        this(boss, 0, executor);
    }

    public ChannelFuture bind(int port, TcpInitializer initializer) {
        this.serverBootstrap.childHandler(new TcpChannelInitializer(initializer));
        return this.serverBootstrap.bind(port);
    }
}
