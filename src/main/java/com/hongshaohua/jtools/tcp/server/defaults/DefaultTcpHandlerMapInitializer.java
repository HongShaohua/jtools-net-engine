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
    private TcpServerDecoder decoder;
    private DefaultTcpHandlerMapEncoder encoder;

    public DefaultTcpHandlerMapInitializer(DefaultTcpHandlerMap handlerMap) {
        this.handlerMap = handlerMap;
        this.decoder = new TcpServerDecoder(this.handlerMap);
        this.encoder = new DefaultTcpHandlerMapEncoder(this.handlerMap);
    }

    @Override
    public TcpServerDecoder decoder() {
        return this.decoder;
    }

    @Override
    public TcpServerEncoder<DefaultTcpMsg> encoder() {
        return this.encoder;
    }

    @Override
    public TcpServerHandler<DefaultTcpMsg> handler() {
        return this.handlerMap;
    }
}
