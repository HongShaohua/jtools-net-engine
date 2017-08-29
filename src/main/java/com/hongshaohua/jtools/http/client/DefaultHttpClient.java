package com.hongshaohua.jtools.http.client;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.*;
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
        PoolingHttpClientConnectionManager cm = null;

        if(this.proxy()) {
            //需要设置代理
            Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new ProxyConnectionSocketFactory())
                    .register("https", new ProxySSLConnectionSocketFactory(SSLContexts.createSystemDefault()))
                    .build();
            cm = new PoolingHttpClientConnectionManager(reg, new FakeDnsResolver());
        } else {
            cm = new PoolingHttpClientConnectionManager();
        }

        cm.setMaxTotal(this.connectionCount);
        cm.setDefaultMaxPerRoute(this.connectionCount);
        return cm;
    }

    static class FakeDnsResolver implements DnsResolver {
        @Override
        public InetAddress[] resolve(String host) throws UnknownHostException {
            // Return some fake DNS record for every request, we won't be using it
            return new InetAddress[] { InetAddress.getByAddress(new byte[] { 1, 1, 1, 1 }) };
        }
    }

    static class ProxyConnectionSocketFactory extends PlainConnectionSocketFactory {
        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }

        @Override
        public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                    InetSocketAddress localAddress, HttpContext context) throws IOException {
            // Convert address to unresolved
            InetSocketAddress unresolvedRemote = InetSocketAddress
                    .createUnresolved(host.getHostName(), remoteAddress.getPort());
            return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);
        }
    }

    static class ProxySSLConnectionSocketFactory extends SSLConnectionSocketFactory {

        public ProxySSLConnectionSocketFactory(final SSLContext sslContext) {
            // You may need this verifier if target site's certificate is not secure
            //super(sslContext, ALLOW_ALL_HOSTNAME_VERIFIER);
            super(sslContext);
        }

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }

        @Override
        public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                    InetSocketAddress localAddress, HttpContext context) throws IOException {
            // Convert address to unresolved
            InetSocketAddress unresolvedRemote = InetSocketAddress
                    .createUnresolved(host.getHostName(), remoteAddress.getPort());
            return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);
        }
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
