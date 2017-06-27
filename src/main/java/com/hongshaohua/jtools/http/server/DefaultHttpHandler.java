package com.hongshaohua.jtools.http.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * Created by Aska on 2017/6/15.
 */
public abstract class DefaultHttpHandler implements ChannelReadHandler {

    private String name;

    public DefaultHttpHandler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected String getContentFromRequest(FullHttpRequest request) throws Exception {
        return request.content().toString(CharsetUtil.UTF_8);
    }

    protected String getOriginalIp(FullHttpRequest request) throws Exception {
        return request.headers().get("ORIGINAL-IP");
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

    protected FullHttpResponse createResponse(HttpResponseStatus status, String content) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.wrappedBuffer(content.getBytes(CharsetUtil.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    protected FullHttpResponse createResponse(String res) throws Exception {
        return createResponse(HttpResponseStatus.OK, res);
    }

    protected FullHttpResponse createResponse(HttpResponseStatus status) throws Exception {
        return new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status);
    }

    protected void beforeHandle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

    }

    protected abstract FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception;

    protected FullHttpResponse afterHandle(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) throws Exception {
        return response;
    }

    protected void write(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) throws Exception {
        ctx.writeAndFlush(response);
    }

    protected void handleProcess(ChannelHandlerContext ctx, FullHttpRequest request, String uri) throws Exception {
        this.beforeHandle(ctx, request);
        FullHttpResponse response = this.handle(ctx, request);
        response = this.afterHandle(ctx, request, response);
        this.write(ctx, request, response);
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest)msg;
        this.handleProcess(ctx, request, request.uri());
    }
}
