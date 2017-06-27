package com.hongshaohua.jtools.http.server.initer;

import com.hongshaohua.jtools.common.initer.IniterTaskTemplate;
import com.hongshaohua.jtools.http.server.ChannelReadHandler;
import com.hongshaohua.jtools.http.server.HttpServer;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Aska on 2017/6/22.
 */
public abstract class DefaultHttpServerIniterTask extends IniterTaskTemplate<HttpServer> {

    private int boss;
    private int work;
    private Map<Integer, ChannelReadHandler> handlerMap;

    public DefaultHttpServerIniterTask(int boss, int work, Map<Integer, ChannelReadHandler> handlerMap) {
        this.boss = boss;
        this.work = work;
        this.handlerMap = handlerMap;
    }

    @Override
    public void init() throws Exception {
        HttpServer httpServer = new HttpServer(boss, work, null);

        Iterator<Map.Entry<Integer, ChannelReadHandler>> it = handlerMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, ChannelReadHandler> entry = it.next();
            httpServer.bind(entry.getKey(), entry.getValue());
        }

        this.finish(httpServer);
    }
}
