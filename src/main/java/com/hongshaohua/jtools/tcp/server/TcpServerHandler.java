package com.hongshaohua.jtools.tcp.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Aska on 2018/1/30.
 */
public interface TcpServerHandler<T> {

    public TcpServerDecoder newDecoder();

    public TcpServerEncoder<T> newEncoder();

    /**建立连接*/
    public void connect(ChannelHandlerContext ctx) throws Exception;

    /**消息分包，从一整段数据流中分出单个数据块*/
    public ByteBuf unpack(ChannelHandlerContext ctx, ByteBuf in) throws Exception;

    /**消息解码，比如解密，序列化*/
    public T decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception;

    /**收到消息*/
    public void received(ChannelHandlerContext ctx, T msg) throws Exception;

    /**消息编码，比如加密，或者反序列化*/
    public ByteBuf encode(ChannelHandlerContext ctx, T msg) throws Exception;

    /**断开连接*/
    public void disconnect(ChannelHandlerContext ctx) throws Exception;

    /**网络引擎异常*/
    public void exception(ChannelHandlerContext ctx, Throwable cause) throws Exception;
}
