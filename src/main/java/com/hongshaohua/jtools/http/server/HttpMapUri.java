package com.hongshaohua.jtools.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shaoh on 2017/5/2.
 */
public class HttpMapUri extends HttpMapping {

    private String name;

    private Map<String, HttpMapUri> handlers;

    public HttpMapUri(String name, HttpMapUri... handlers) {
        this(handlers);
        this.name = name;
    }

    public HttpMapUri(HttpMapUri... handlers) {
        this.handlers = new HashMap<String, HttpMapUri>();
        for(HttpMapUri handler : handlers) {
            this.handlers.put(handler.getName(), handler);
        }
    }

    public String getName() {
        return name;
    }

    private HttpHandler getHttpHandler(String path) {
        if(path.length() <= 1) {
            return this;
        }
        int nameStart = 1;
        int nameEnd = path.indexOf("/", nameStart);
        String name = null;
        String subPath = null;
        if(nameEnd < 0) {
            name = path.substring(nameStart);
            subPath = "";
        } else {
            name = path.substring(nameStart, nameEnd);
            subPath = path.substring(nameEnd);
        }
        HttpMapUri handler = this.handlers.get(name);
        if(handler == null) {
            return null;
        }
        return handler.getHttpHandler(subPath);
    }

    public HttpHandler httpHandleMapping(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //解析uri，获取路径
        URI uri = new URI(request.uri());
        String path = uri.getPath();
        return this.getHttpHandler(path);
    }

    @Override
    public FullHttpResponse httpHandleNotFound(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
    }

    @Override
    public void httpWrite(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) throws Exception {
        ctx.writeAndFlush(response);
    }

    public String getOriginalIp(FullHttpRequest request) {
        return request.headers().get("ORIGINAL-IP");
    }

    public String getIp(ChannelHandlerContext ctx) {
        String str = ctx.channel().remoteAddress().toString();
        int index = str.indexOf("/");
        if(index >= 0) {
            str = str.substring(index + 1);
        }
        index = str.indexOf(":");
        if(index >= 0) {
            str = str.substring(0, index);
        }
        return str;
    }

    public Map<String, String> getUrlParams(FullHttpRequest request) throws Exception {
        String strUri = request.uri();
        URI uri = new URI(strUri);
        String path = uri.getPath();


        Map<String, String> params = new HashMap<String, String>();

        //if(questionMarkIndex < 0) {
        //    return params;
        //}
        //
        //String paramsStr = uri.substring(questionMarkIndex + 1);
        //
        //String[] paramStrs = paramsStr.split("&");
        //for(String paramStr : paramStrs) {
        //    int equalMarkIndex = paramStr.indexOf("=");
        //    String key = paramStr.substring(0, equalMarkIndex);
        //    String value = paramStr.substring(equalMarkIndex + 1);
        //    params.put(key, value);
        //}

        return params;
    }

    public String getContent(FullHttpRequest request) {
        return request.content().toString(CharsetUtil.UTF_8);
    }
}
