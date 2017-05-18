package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Created by shaoh on 2017/5/2.
 */
public abstract class HttpMapping extends HttpHandler {

    public abstract HttpHandler httpHandleMapping(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception;

    public abstract FullHttpResponse httpHandleNotFound(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception;

    @Override
    public FullHttpResponse httpHandle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        HttpHandler httpHandler = this.httpHandleMapping(ctx, request);
        if(httpHandler == null) {
            return this.httpHandleNotFound(ctx, request);
        }
        return httpHandler.httpHandle(ctx, request);
    }
}
