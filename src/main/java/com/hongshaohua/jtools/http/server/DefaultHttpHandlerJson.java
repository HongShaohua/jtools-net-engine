package com.hongshaohua.jtools.http.server;

import com.hongshaohua.jtools.common.json.JsonUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;

/**
 * Created by shaoh on 2017/5/3.
 */
public abstract class DefaultHttpHandlerJson<REQUEST_DATA, RESPONSE_DATA> extends DefaultHttpHandler {

    private final static Logger logger = LoggerFactory.getLogger(DefaultHttpHandlerJson.class);

    private Class<REQUEST_DATA> requestDataClass;
    private Class<RESPONSE_DATA> responseDataClass;

    public DefaultHttpHandlerJson(String name) {
        super(name);
        this.init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        this.requestDataClass = (Class<REQUEST_DATA>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.responseDataClass = (Class<RESPONSE_DATA>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected REQUEST_DATA str2RequestData(String str) throws Exception {
        return JsonUtils.json2Obj(str, this.requestDataClass);
    }

    protected String requestData2Str(REQUEST_DATA requestData) throws Exception {
        return JsonUtils.obj2Json(requestData);
    }

    protected RESPONSE_DATA str2ResponseData(String str) throws Exception {
        return JsonUtils.json2Obj(str, this.responseDataClass);
    }

    protected String responseData2Str(RESPONSE_DATA responseData) throws Exception {
        return JsonUtils.obj2Json(responseData);
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

    protected REQUEST_DATA getRequestData(FullHttpRequest request) throws Exception {
        String content = this.getContentFromRequest(request);
        //System.out.println("request content : " + content);
        return str2RequestData(content);
    }

    protected abstract RESPONSE_DATA handle(ChannelHandlerContext ctx, FullHttpRequest request, REQUEST_DATA request_data) throws Exception;

    protected FullHttpResponse getResponse(RESPONSE_DATA responseData) throws Exception {
        if(responseData == null) {
            return this.createResponse(HttpResponseStatus.NOT_FOUND);
        }
        String res = responseData2Str(responseData);
        if(res == null) {
            return this.createResponse(HttpResponseStatus.NOT_FOUND);
        }
        return this.createResponse(res);
    }

    @Override
    protected FullHttpResponse handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        REQUEST_DATA requestData = null;
        try {
            requestData = this.getRequestData(request);
        } catch (Exception e) {
            logger.error("request format error : " + e.getMessage());
            return this.createResponse(HttpResponseStatus.BAD_REQUEST, "request format error");
        }
        RESPONSE_DATA responseData = this.handle(ctx, request, requestData);
        return this.getResponse(responseData);
    }
}
