package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {
	
	private ChannelReadHandler channelReadHandler;
	
	public HttpServerInboundHandler(ChannelReadHandler channelReadHandler, ExecutorService executors) {
		this.channelReadHandler = channelReadHandler;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		channelReadHandler.channelRead(ctx, msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
