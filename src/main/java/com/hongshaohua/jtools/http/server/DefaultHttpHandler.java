package com.hongshaohua.jtools.http.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Aska on 2017/6/15.
 */
public abstract class DefaultHttpHandler implements ChannelReadHandler {

    private final static Logger logger = LoggerFactory.getLogger(DefaultHttpHandler.class);

    private String name;

    public DefaultHttpHandler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected String getIp(ChannelHandlerContext ctx) throws Exception {
        String str = ctx.channel().remoteAddress().toString();
        int index = str.indexOf("/");
        if(index >= 0) {
            str = str.substring(index + 1);
        }
        index = str.indexOf(":");
        if(index >= 0) {
            str = str.substring(0, index);
        }
        return str;
    }

    protected String getOriginalIpFromRequest(FullHttpRequest request) throws Exception {
        return request.headers().get("ORIGINAL-IP");
    }

    protected FullHttpResponse createResponse(HttpResponseStatus status) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.wrappedBuffer("".getBytes(CharsetUtil.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    protected void handleBefore(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

    }

    protected abstract FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception;

    protected void handleAfter(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) throws Exception {

    }

    protected void writeResponse(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
        ctx.writeAndFlush(response);
    }

    protected void handleProcess(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        this.handleBefore(ctx, request);
        FullHttpResponse response = this.handle(ctx, request);
        this.handleAfter(ctx, request, response);
        this.writeResponse(ctx, response);
    }

    protected void handleMap(ChannelHandlerContext ctx, FullHttpRequest request, String uri) throws Exception {
        this.handleProcess(ctx, request);
    }

    protected void exception(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage(), cause);
        try {
            FullHttpResponse response = this.createResponse(HttpResponseStatus.BAD_REQUEST);
            this.writeResponse(ctx, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest request = (FullHttpRequest)msg;
            this.handleMap(ctx, request, request.uri());
        } catch (Exception e) {
            this.exception(ctx, e);
        }
    }
}
