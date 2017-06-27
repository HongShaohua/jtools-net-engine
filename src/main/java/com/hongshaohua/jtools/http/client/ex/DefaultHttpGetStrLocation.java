package com.hongshaohua.jtools.http.client.ex;

import com.hongshaohua.jtools.http.client.DefaultHttpGetString;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

import java.net.URI;

/**
 * Created by Aska on 2017/6/19.
 */
public class DefaultHttpGetStrLocation extends DefaultHttpGetString {

    private String host;
    private String uri;
    private String uriPath;

    public DefaultHttpGetStrLocation(String url, String charset) {
        super(url, charset);
    }

    public DefaultHttpGetStrLocation(String url) {
        super(url);
    }

    public String host() {
        return host;
    }

    protected void host(String host) {
        this.host = host;
    }

    public String uri() {
        return uri;
    }

    protected void uri(String uri) {
        this.uri = uri;
    }

    public String uriPath() {
        return uriPath;
    }

    protected void uriPath(String uriPath) {
        this.uriPath = uriPath;
    }

    @Override
    protected HttpContext createContext() throws Exception {
        return new BasicHttpContext();
    }

    @Override
    protected boolean response(HttpResponse response) throws Exception {
        if(!super.response(response)) {
            return false;
        }

        HttpHost httpHost = (HttpHost) context().getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
        HttpUriRequest httpUriRequest = (HttpUriRequest) context().getAttribute(HttpCoreContext.HTTP_REQUEST);
        this.host(httpHost.toURI());

        URI uri = httpUriRequest.getURI();
        this.uri(uri.toString());
        this.uriPath(uri.getPath());
        return true;
    }
}
