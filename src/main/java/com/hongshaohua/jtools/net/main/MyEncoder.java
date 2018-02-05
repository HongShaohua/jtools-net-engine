package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.tcp.server.TcpServerEncoder;
import com.hongshaohua.jtools.tcp.server.TcpServerHandler;
import io.netty.buffer.ByteBuf;

/**
 * Created by Aska on 2018/2/5.
 */
public class MyEncoder extends TcpServerEncoder<ByteBuf> {

    public MyEncoder(TcpServerHandler handler) {
        super(handler);
    }
}
