package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Created by shaoh on 2017/5/2.
 */
public abstract class HttpMapUriHandler extends HttpMapUri {

    public HttpMapUriHandler(String name) {
        super(name);
    }

    @Override
    public abstract FullHttpResponse httpHandle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception;
}
