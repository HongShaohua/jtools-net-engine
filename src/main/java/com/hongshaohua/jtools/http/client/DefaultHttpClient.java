package com.hongshaohua.jtools.http.client;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.List;

/**
 * Created by Aska on 2017/6/18.
 */
public class DefaultHttpClient extends HttpClientEx {

    private int connectionCount = 0;
    private List<Header> headers = null;
    private CookieStore cookieStore;
    private RequestConfig requestConfig;

    public DefaultHttpClient() {

    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    @Override
    public boolean open(HttpClientBuilder builder) {
        return super.open(builder);
    }

    private PoolingHttpClientConnectionManager connectionManager() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(this.connectionCount);
        cm.setDefaultMaxPerRoute(this.connectionCount);
        return cm;
    }

    public boolean open() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        if(this.connectionCount > 0) {
            httpClientBuilder = httpClientBuilder.setConnectionManager(connectionManager());
        }
        if(this.headers != null) {
            httpClientBuilder = httpClientBuilder.setDefaultHeaders(this.headers);
        }
        if(this.cookieStore != null) {
            httpClientBuilder = httpClientBuilder.setDefaultCookieStore(cookieStore);
        }
        if(this.requestConfig != null) {
            httpClientBuilder = httpClientBuilder.setDefaultRequestConfig(this.requestConfig);
        }
        return this.open(httpClientBuilder);
    }
}
