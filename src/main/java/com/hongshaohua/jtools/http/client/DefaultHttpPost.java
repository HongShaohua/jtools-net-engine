package com.hongshaohua.jtools.http.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

/**
 * Created by Aska on 2017/6/18.
 */
public abstract class DefaultHttpPost extends HttpClientMethod {

    public DefaultHttpPost(String url) {
        super(METHOD_POST, url);
    }

    @Override
    protected HttpGet get() throws Exception {
        return null;
    }

    protected abstract HttpEntity requestEntity() throws Exception;

    @Override
    protected HttpPost post() throws Exception {
        HttpPost httpPost = new HttpPost(url());
        httpPost.setEntity(requestEntity());
        return httpPost;
    }
}
