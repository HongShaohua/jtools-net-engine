package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Aska on 2017/6/19.
 */
public abstract class DefaultHttpHandlerJsonOrigin<REQUEST_DATA, RESPONSE_DATA> extends DefaultHttpHandlerJson<REQUEST_DATA, RESPONSE_DATA> {

    private final static Logger logger = LoggerFactory.getLogger(DefaultHttpHandlerJsonOrigin.class);

    public DefaultHttpHandlerJsonOrigin(String name) {
        super(name);
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
