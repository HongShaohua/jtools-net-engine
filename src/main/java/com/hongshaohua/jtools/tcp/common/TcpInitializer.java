package com.hongshaohua.jtools.tcp.common;

/**
 * Created by Aska on 2018/2/6.
 */
public interface TcpInitializer {

    public TcpDecoder decoder();

    public TcpEncoder encoder();

    public TcpHandler handler();
}
