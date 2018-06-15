package com.hongshaohua.jtools.tcp.initer;

import com.hongshaohua.jtools.common.initer.IniterTaskTemplate;
import com.hongshaohua.jtools.tcp.server.TcpServer;
import com.hongshaohua.jtools.tcp.defaults.DefaultTcpHandlerMapInitializer;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Aska on 2017/6/22.
 */
public abstract class DefaultTcpServerIniterTask extends IniterTaskTemplate<TcpServer> {

    private int boss;
    private int work;
    private Map<Integer, DefaultTcpHandlerMapInitializer> initializerMap;

    public DefaultTcpServerIniterTask(int boss, int work, Map<Integer, DefaultTcpHandlerMapInitializer> initializerMap) {
        this.boss = boss;
        this.work = work;
        this.initializerMap = initializerMap;
    }

    @Override
    public void init() throws Exception {
        TcpServer tcpServer = new TcpServer(boss, work, null);

        Iterator<Map.Entry<Integer, DefaultTcpHandlerMapInitializer>> it = initializerMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, DefaultTcpHandlerMapInitializer> entry = it.next();
            tcpServer.bind(entry.getKey(), entry.getValue());
        }

        this.finish(tcpServer);
    }
}
