package com.hongshaohua.jtools.http.server;

import com.hongshaohua.jtools.common.json.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.lang.reflect.ParameterizedType;

/**
 * Created by shaoh on 2017/5/3.
 */
public abstract class HttpDataTemplateHandler<REQUEST_DATA, RESPONSE_DATA> extends HttpStrHandler {

    private Class<REQUEST_DATA> requestDataClass;
    private Class<RESPONSE_DATA> responseDataClass;

    public HttpDataTemplateHandler(String name) {
        super(name);
        this.init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        this.requestDataClass = (Class<REQUEST_DATA>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.responseDataClass = (Class<RESPONSE_DATA>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public REQUEST_DATA json2RequestData(String json) throws Exception {
        return JsonUtils.json2Obj(json, this.requestDataClass);
    }

    public String requestData2Json(REQUEST_DATA requestData) throws Exception {
        return JsonUtils.obj2Json(requestData);
    }

    public RESPONSE_DATA json2ResponseData(String json) throws Exception {
        return JsonUtils.json2Obj(json, this.responseDataClass);
    }

    public String responseData2Json(RESPONSE_DATA responseData) throws Exception {
        return JsonUtils.obj2Json(responseData);
    }

    public abstract RESPONSE_DATA dataHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, REQUEST_DATA requestData) throws Exception;

    @Override
    public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, String content) throws Exception {
        REQUEST_DATA requestData = json2RequestData(content);
        RESPONSE_DATA responseData = this.dataHttpHandle(ctx, request, requestData);
        return responseData2Json(responseData);
    }
}
