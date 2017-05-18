package com.hongshaohua.jtools.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ExecutorService;

public class HttpServer {
	
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ServerBootstrap serverBootstrap;
	private ExecutorService executor;
	
	public HttpServer(int boss, int worker, ExecutorService executor) {
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
	
	public HttpServer(int boss, ExecutorService executor) {
		this(boss, 0, executor);
	}
	
	public ChannelFuture bind(int port, ChannelReadHandler channelReadHandler) {
		this.serverBootstrap.childHandler(new HttpServerChannelInitializer(channelReadHandler, this.executor));
		return this.serverBootstrap.bind(port);
	}
}
