package com.hongshaohua.jtools.http.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

/**
 * Created by Aska on 2017/6/18.
 */
public abstract class HttpClientMethod {

    protected static final int METHOD_GET = 1;
    protected static final int METHOD_POST = 2;
    private int method;

    private String url;

    private int statusCode;
    private HttpHost target;
    private HttpContext context;

    public HttpClientMethod(int method, String url) {
        this.method = method;
        this.url = url;
    }

    public String url() {
        return url;
    }

    protected int method() {
        return method;
    }

    public boolean isOk() {
        return this.statusCode == HttpStatus.SC_OK;
    }

    public int statusCode() {
        return this.statusCode;
    }

    protected HttpHost createTarget() throws Exception {
        return null;
    }

    protected HttpHost target() throws Exception {
        if(this.target == null) {
            this.target = this.createTarget();
        }
        return this.target;
    }

    protected HttpContext createContext() throws Exception {
        return null;
    }

    protected HttpContext context() throws Exception {
        if(this.context == null) {
            this.context = this.createContext();
        }
        return this.context;
    }

    protected HttpUriRequest request() throws Exception {
        switch (this.method()) {
            case METHOD_GET: return get();
            case METHOD_POST: return post();
            default:return null;
        }
    }

    protected abstract HttpGet get() throws Exception;

    protected abstract HttpPost post() throws Exception;

    protected abstract boolean responseEntiry(HttpEntity entity) throws Exception;

    protected boolean response(HttpResponse response) throws Exception {
        if(response == null) {
            return false;
        }
        this.statusCode = response.getStatusLine().getStatusCode();
        return responseEntiry(response.getEntity());
    }
}
