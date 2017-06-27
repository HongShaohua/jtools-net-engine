package com.hongshaohua.jtools.http.client;

import com.hongshaohua.jtools.common.json.JsonUtils;
import org.apache.http.HttpEntity;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Aska on 2017/6/19.
 */
public class DefaultHttpGetJson<RESPONSE_DATA> extends DefaultHttpGetString {

    private Class<RESPONSE_DATA> responseDataClass;
    private RESPONSE_DATA responseData;

    public DefaultHttpGetJson(String url, String charset) {
        super(url, charset);
        this.init();
    }

    public DefaultHttpGetJson(String url) {
        super(url);
        this.init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        this.responseDataClass = (Class<RESPONSE_DATA>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected RESPONSE_DATA str2ResponseData(String str) throws Exception {
        return JsonUtils.json2Obj(str, this.responseDataClass);
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
