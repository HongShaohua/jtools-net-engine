package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * Created by Aska on 2017/6/22.
 */
public class DefaultHttpHandlerMapOrigin extends DefaultHttpHandlerMap {

    public DefaultHttpHandlerMapOrigin(DefaultHttpHandler... handlers) {
        super(handlers);
    }

    public DefaultHttpHandlerMapOrigin(String name, DefaultHttpHandler... handlers) {
        super(name, handlers);
    }

    @Override
    protected void handleAfter(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) throws Exception {
        super.handleAfter(ctx, request, response);
        if(HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
    }

    @Override
    protected void writeResponse(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "post");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "x-requested-with,content-type");
        super.writeResponse(ctx, response);
    }
}
