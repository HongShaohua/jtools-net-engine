package com.hongshaohua.jtools.http.client;

import com.hongshaohua.jtools.common.json.JsonUtils;
import org.apache.http.HttpEntity;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Aska on 2017/6/19.
 */
public class DefaultHttpPostJson<REQUEST_DATA, RESPONSE_DATA> extends DefaultHttpPostString {

    private Class<REQUEST_DATA> requestDataClass;
    private Class<RESPONSE_DATA> responseDataClass;
    private REQUEST_DATA requestData;
    private RESPONSE_DATA responseData;

    public DefaultHttpPostJson(String url, String charset) {
        super(url, charset);
        this.init();
    }

    public DefaultHttpPostJson(String url) {
        super(url);
        this.init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        this.requestDataClass = (Class<REQUEST_DATA>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.responseDataClass = (Class<RESPONSE_DATA>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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

    public REQUEST_DATA requestData() {
        return requestData;
    }

    public void requestData(REQUEST_DATA requestData) throws Exception {
        this.requestContent(requestData2Str(requestData));
    }

    public RESPONSE_DATA responseData() {
        return responseData;
    }

    protected void responseData(RESPONSE_DATA responseData) {
        this.responseData = responseData;
    }

    @Override
    protected boolean responseEntiry(HttpEntity entity) throws Exception {
        if(!super.responseEntiry(entity)) {
            return false;
        }
        this.responseData(this.str2ResponseData(this.responseContent()));
        return true;
    }
}
