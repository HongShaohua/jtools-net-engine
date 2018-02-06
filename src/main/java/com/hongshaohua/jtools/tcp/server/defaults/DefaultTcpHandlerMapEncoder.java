package com.hongshaohua.jtools.tcp.server.defaults;

import com.hongshaohua.jtools.tcp.server.TcpServerEncoder;

/**
 * Created by Aska on 2018/2/5.
 */
public class DefaultTcpHandlerMapEncoder extends TcpServerEncoder<DefaultTcpMsg> {

    public DefaultTcpHandlerMapEncoder(DefaultTcpHandlerMap handlerMap) {
        super(handlerMap);
    }
}
