package com.hongshaohua.jtools.http.client;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

/**
 * Created by Aska on 2017/6/18.
 */
public abstract class DefaultHttpGet extends HttpClientMethod {

    public DefaultHttpGet(String url) {
        super(METHOD_GET, url);
    }

    @Override
    protected HttpGet get() throws Exception {
        return new HttpGet(url());
    }

    @Override
    protected final HttpPost post() throws Exception {
        return null;
    }
}
