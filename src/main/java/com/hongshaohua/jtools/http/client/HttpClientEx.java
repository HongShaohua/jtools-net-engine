package com.hongshaohua.jtools.http.client;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Aska on 2017/6/18.
 */
public class HttpClientEx {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientEx.class);

    private CloseableHttpClient httpClient = null;

    private String proxyHost;
    private int proxyPort;

    public HttpClientEx() {
    }

    protected CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxy(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    public boolean proxy() {
        if(this.proxyHost != null && !this.proxyHost.trim().isEmpty() && this.proxyPort > 0) {
            return true;
        }
        return false;
    }

    public boolean open(HttpClientBuilder builder) {
        if(this.httpClient != null) {
            return false;
        }
        this.httpClient = builder.build();
        return true;
    }

    public boolean execute(HttpClientMethod method) {
        if(this.httpClient == null) {
            return false;
        }
        boolean success;
        CloseableHttpResponse response = null;
        try {
            HttpUriRequest request = method.request();
            if(request == null) {
                return false;
            }
            HttpHost target = method.target();
            HttpContext context = method.context();

            if(this.proxy()) {
                if(context == null) {
                    context = HttpClientContext.create();
                }
                InetSocketAddress socksaddr = new InetSocketAddress(this.proxyHost, this.proxyPort);
                context.setAttribute("socks.address", socksaddr);
            }

            if(target != null && context != null) {
                response = this.httpClient.execute(target, request, context);
            } else if(target != null && context == null) {
                response = this.httpClient.execute(target, request);
            } else if(target == null && context != null) {
                response = this.httpClient.execute(request, context);
            } else if(target == null && context == null) {
                response = this.httpClient.execute(request);
            }
            success = method.response(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            success = false;
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return success;
    }

    public boolean close() {
        if(this.httpClient == null) {
            return true;
        }
        try {
            this.httpClient.close();
            this.httpClient = null;
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
