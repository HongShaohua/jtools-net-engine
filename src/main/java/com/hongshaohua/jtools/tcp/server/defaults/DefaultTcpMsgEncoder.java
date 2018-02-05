package com.hongshaohua.jtools.tcp.server.defaults;

import com.hongshaohua.jtools.tcp.server.TcpServerEncoder;
import com.hongshaohua.jtools.tcp.server.TcpServerHandler;

/**
 * Created by Aska on 2018/2/5.
 */
public class DefaultTcpMsgEncoder<T extends DefaultTcpMsg> extends TcpServerEncoder<T> {

    public DefaultTcpMsgEncoder(TcpServerHandler handler) {
        super(handler);
    }
}
