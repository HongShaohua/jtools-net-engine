package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Created by shaoh on 2017/5/2.
 */
public abstract class HttpHandler implements ChannelReadHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest)msg;
        FullHttpResponse response = this.httpHandle(ctx, request);
        this.httpWrite(ctx, request, response);
    }

    public abstract FullHttpResponse httpHandle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception;

    public abstract void httpWrite(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) throws Exception;
}
