package com.hongshaohua.jtools.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Aska on 2017/5/17.
 */
public class HttpClientX {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientX.class);

    private CloseableHttpClient httpClient = null;

    public HttpClientX(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClientX(HttpClientBuilder builder) {
        this(builder.build());
    }

    public interface HttpGetHandler {
        public String url();
        public boolean response(HttpResponse response);
    }

    public synchronized boolean get(HttpGetHandler handler) {
        if(this.httpClient == null) {
            return false;
        }
        CloseableHttpResponse response = null;
        boolean success = false;
        HttpGet httpGet = new HttpGet(handler.url());
        try {
            response = this.httpClient.execute(httpGet);
            success = handler.response(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            success = false;
        } finally {
            try {
                if(response != null) {
                    response.close();
                }
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }
        return success;
    }

    public interface HttpPostHandler {
        public String url();
        public String content();
        public String charset();
        public boolean response(HttpResponse response);
    }

    public synchronized boolean post(HttpPostHandler handler) {
        if(this.httpClient == null) {
            return false;
        }
        CloseableHttpResponse response = null;
        boolean success = false;
        HttpPost httpPost = new HttpPost(handler.url());
        StringEntity entity = new StringEntity(handler.content(), handler.charset());
        httpPost.setEntity(entity);
        try {
            response = this.httpClient.execute(httpPost);
            success = handler.response(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            success = false;
        } finally {
            try {
                if(response != null) {
                    response.close();
                }
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }
        return success;
    }

    public synchronized boolean close() {
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
