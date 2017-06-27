package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;

/**
 * Created by Aska on 2017/6/22.
 */
public class DefaultHttpHandlerMapJsonOrigin extends DefaultHttpHandlerMap {

    public DefaultHttpHandlerMapJsonOrigin(DefaultHttpHandler... handlers) {
        super(handlers);
    }

    public DefaultHttpHandlerMapJsonOrigin(String name, DefaultHttpHandler... handlers) {
        super(name, handlers);
    }

    @Override
    protected FullHttpResponse afterHandle(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) throws Exception {
        response = super.afterHandle(ctx, request, response);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "post");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "x-requested-with,content-type");
        return response;
    }
}
