package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.concurrent.ExecutorService;

public class HttpServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private ChannelReadHandler handler;
	private ExecutorService executors;
	
	public HttpServerChannelInitializer(ChannelReadHandler handler, ExecutorService executors) {
		this.handler = handler;
		this.executors = executors;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new HttpRequestDecoder());
		ch.pipeline().addLast(new HttpObjectAggregator(4096));
		ch.pipeline().addLast(new HttpResponseEncoder());
		ch.pipeline().addLast(new ChunkedWriteHandler());
		ch.pipeline().addLast(new HttpServerInboundHandler(handler, executors));
	}
}
