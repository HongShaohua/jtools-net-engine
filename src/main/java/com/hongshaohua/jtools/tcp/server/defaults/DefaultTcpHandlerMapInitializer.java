package com.hongshaohua.jtools.tcp.server.defaults;

import com.hongshaohua.jtools.tcp.server.TcpServerDecoder;
import com.hongshaohua.jtools.tcp.server.TcpServerEncoder;
import com.hongshaohua.jtools.tcp.server.TcpServerHandler;
import com.hongshaohua.jtools.tcp.server.TcpServerInitializer;

/**
 * Created by Aska on 2018/2/6.
 */
public class DefaultTcpHandlerMapInitializer implements TcpServerInitializer {

    private DefaultTcpHandlerMap handlerMap;

    public DefaultTcpHandlerMapInitializer(DefaultTcpHandlerMap handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public TcpServerDecoder decoder() {
        return new TcpServerDecoder(this.handlerMap);
    }

    @Override
    public TcpServerEncoder<DefaultTcpMsg> encoder() {
        return new DefaultTcpHandlerMapEncoder(this.handlerMap);
    }

    @Override
    public TcpServerHandler<DefaultTcpMsg> handler() {
        return this.handlerMap;
    }
}
