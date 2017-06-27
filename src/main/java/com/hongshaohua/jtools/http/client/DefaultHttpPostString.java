package com.hongshaohua.jtools.http.client;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

/**
 * Created by Aska on 2017/6/18.
 */
public class DefaultHttpPostString extends DefaultHttpPost {

    private String charset;
    private String requestContent;
    private String responseContent;

    public DefaultHttpPostString(String url, String charset) {
        super(url);
        this.charset = charset;
    }

    public DefaultHttpPostString(String url) {
        this(url, "utf-8");
    }

    public String charset() {
        return charset;
    }

    public String requestContent() {
        return requestContent;
    }

    public void requestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public String responseContent() {
        return responseContent;
    }

    protected void responseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    @Override
    protected HttpEntity requestEntity() throws Exception {
        return new StringEntity(requestContent(), charset());
    }

    @Override
    protected boolean responseEntiry(HttpEntity entity) throws Exception {
        this.responseContent(EntityUtils.toString(entity, this.charset()));
        return true;
    }
}
