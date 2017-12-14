package com.hongshaohua.jtools.http.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * Created by Aska on 2017/10/10.
 */
public abstract class DefaultHttpHandlerBytes extends DefaultHttpHandler {

    private final static Logger logger = LoggerFactory.getLogger(DefaultHttpHandlerBytes.class);

    public DefaultHttpHandlerBytes(String name) {
        super(name);
    }

    protected FullHttpResponse createResponse(HttpResponseStatus status, byte[] bytes, String type) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.wrappedBuffer(bytes));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, type);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    protected FullHttpResponse createResponse(byte[] bytes, String type) throws Exception {
        return this.createResponse(HttpResponseStatus.OK, bytes, type);
    }

    protected byte[] getRequestBytes(FullHttpRequest request) throws Exception {
        ByteBuf byteBuf = request.content();
        int len = byteBuf.readableBytes();
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    protected abstract FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request, byte[] requestBytes) throws Exception;

    protected FullHttpResponse getResponse(byte[] bytes, String type) throws Exception {
        if(bytes == null) {
            return this.createResponse(HttpResponseStatus.NOT_FOUND);
        }
        return this.createResponse(bytes, type);
    }

    @Override
    protected FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        byte[] requestBytes = null;
        try {
            requestBytes = this.getRequestBytes(request);
        } catch (Exception e) {
            logger.error("request format error : " + e.getMessage());
            return this.createResponse(HttpResponseStatus.BAD_REQUEST, "".getBytes(Charset.forName("UTF-8")), "text/plain; charset=UTF-8");
        }
        return this.handle(ctx, request, requestBytes);
    }
}
