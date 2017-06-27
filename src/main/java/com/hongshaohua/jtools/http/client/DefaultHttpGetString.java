package com.hongshaohua.jtools.http.client;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * Created by Aska on 2017/6/18.
 */
public class DefaultHttpGetString extends DefaultHttpGet {

    private String charset;
    private String responseContent;

    public DefaultHttpGetString(String url, String charset) {
        super(url);
        this.charset = charset;
    }

    public DefaultHttpGetString(String url) {
        this(url, "utf-8");
    }

    public String charset() {
        return charset;
    }

    public String responseContent() {
        return responseContent;
    }

    protected void responseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    @Override
    protected boolean responseEntiry(HttpEntity entity) throws Exception {
        this.responseContent(EntityUtils.toString(entity, this.charset()));
        return true;
    }
}
