package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shaoh on 2017/5/2.
 */
public class DefaultHttpHandlerMap extends DefaultHttpHandler {

    private Map<String, DefaultHttpHandler> handlerMap;

    public DefaultHttpHandlerMap(DefaultHttpHandler... handlers) {
        this(null, handlers);
    }

    public DefaultHttpHandlerMap(String name, DefaultHttpHandler... handlers) {
        super(name);
        this.handlerMap = new HashMap<>();
        for(DefaultHttpHandler handler : handlers) {
            this.handlerMap.put(handler.getName(), handler);
        }
    }

    @Override
    protected FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        return null;
    }

    private class UriPart {
        String name;
        String subUri;

        UriPart(String name, String subUri) {
            this.name = name;
            this.subUri = subUri;
        }
    }

    private UriPart parseUri(String uri) {
        if(uri == null || uri.trim().isEmpty()) {
            return null;
        }
        int nameStart = 1;
        int nameEnd = uri.indexOf("/", nameStart);
        String name = null;
        String subUri = null;
        if(nameEnd < 0) {
            name = uri.substring(nameStart);
            subUri = "";
        } else {
            name = uri.substring(nameStart, nameEnd);
            subUri = uri.substring(nameEnd);
        }
        return new UriPart(name, subUri);
    }

    public FullHttpResponse handlerNotFound(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        return this.createResponse(HttpResponseStatus.NOT_FOUND);
    }

    @Override
    protected void handleProcess(ChannelHandlerContext ctx, FullHttpRequest request, String uri) throws Exception {
        UriPart uriPart = this.parseUri(uri);
        if(uriPart == null) {
            this.write(ctx, this.handlerNotFound(ctx, request));
            return;
        }
        DefaultHttpHandler handler = this.handlerMap.get(uriPart.name);
        if(handler == null) {
            this.write(ctx, this.handlerNotFound(ctx, request));
            return;
        }
        handler.handleProcess(ctx, request, uriPart.subUri);
    }
}
