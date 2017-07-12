package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by shaoh on 2017/5/3.
 */
public interface ChannelReadHandler {

    public void handle(ChannelHandlerContext ctx, Object msg);

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause);

}
