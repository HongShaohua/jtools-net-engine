package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * Created by Aska on 2017/6/19.
 */
public abstract class DefaultHttpHandlerJsonOrigin<REQUEST_DATA, RESPONSE_DATA> extends DefaultHttpJsonHandler<REQUEST_DATA, RESPONSE_DATA> {

    public DefaultHttpHandlerJsonOrigin(String name) {
        super(name);
    }

    @Override
    protected FullHttpResponse afterHandle(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) throws Exception {
        response = super.afterHandle(ctx, request, response);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "post");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "x-requested-with,content-type");
        if(HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        return response;
    }
}
