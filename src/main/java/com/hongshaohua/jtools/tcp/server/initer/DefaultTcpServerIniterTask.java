package com.hongshaohua.jtools.tcp.server.initer;

import com.hongshaohua.jtools.common.initer.IniterTaskTemplate;
import com.hongshaohua.jtools.tcp.server.TcpServer;
import com.hongshaohua.jtools.tcp.server.TcpServerHandler;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Aska on 2017/6/22.
 */
public abstract class DefaultTcpServerIniterTask extends IniterTaskTemplate<TcpServer> {

    private int boss;
    private int work;
    private Map<Integer, TcpServerHandler> handlerMap;

    public DefaultTcpServerIniterTask(int boss, int work, Map<Integer, TcpServerHandler> handlerMap) {
        this.boss = boss;
        this.work = work;
        this.handlerMap = handlerMap;
    }

    @Override
    public void init() throws Exception {
        TcpServer tcpServer = new TcpServer(boss, work, null);

        Iterator<Map.Entry<Integer, TcpServerHandler>> it = handlerMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, TcpServerHandler> entry = it.next();
            tcpServer.bind(entry.getKey(), entry.getValue());
        }

        this.finish(tcpServer);
    }
}
