package com.hongshaohua.jtools.tcp.server;

/**
 * Created by Aska on 2018/2/6.
 */
public interface TcpServerInitializer {

    public TcpServerDecoder decoder();

    public TcpServerEncoder encoder();

    public TcpServerHandler handler();
}
