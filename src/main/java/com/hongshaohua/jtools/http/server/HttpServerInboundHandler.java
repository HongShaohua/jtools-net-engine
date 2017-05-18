package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(HttpServerInboundHandler.class);
	
	private ChannelReadHandler channelReadHandler;
	
	public HttpServerInboundHandler(ChannelReadHandler channelReadHandler, ExecutorService executors) {
		this.channelReadHandler = channelReadHandler;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		channelReadHandler.handle(ctx, msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error(cause.getMessage(), cause);
		super.exceptionCaught(ctx, cause);
	}
}
