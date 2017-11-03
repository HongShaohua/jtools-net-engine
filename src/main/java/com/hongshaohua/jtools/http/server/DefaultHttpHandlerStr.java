package com.hongshaohua.jtools.http.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Aska on 2017/10/10.
 */
public abstract class DefaultHttpHandlerStr extends DefaultHttpHandler {

    private final static Logger logger = LoggerFactory.getLogger(DefaultHttpHandlerStr.class);

    public DefaultHttpHandlerStr(String name) {
        super(name);
    }

    protected FullHttpResponse createResponse(HttpResponseStatus status, String content) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.wrappedBuffer(content.getBytes(CharsetUtil.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    protected FullHttpResponse createResponse(String res) throws Exception {
        return this.createResponse(HttpResponseStatus.OK, res);
    }

    protected String getContentFromRequest(FullHttpRequest request) throws Exception {
        return request.content().toString(CharsetUtil.UTF_8);
    }

    protected String getRequestStr(FullHttpRequest request) throws Exception {
        String content = this.getContentFromRequest(request);
        return content;
    }

    protected abstract String handle(ChannelHandlerContext ctx, FullHttpRequest request, String requestStr) throws Exception;

    protected FullHttpResponse getResponse(String responseStr) throws Exception {
        if(responseStr == null) {
            return this.createResponse(HttpResponseStatus.NOT_FOUND);
        }
        return this.createResponse(responseStr);
    }

    @Override
    protected FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String requestStr = null;
        try {
            requestStr = this.getRequestStr(request);
        } catch (Exception e) {
            logger.error("request format error : " + e.getMessage());
            return this.createResponse(HttpResponseStatus.BAD_REQUEST, "request format error");
        }
        String responseStr = this.handle(ctx, request, requestStr);
        return this.getResponse(responseStr);
    }
}
