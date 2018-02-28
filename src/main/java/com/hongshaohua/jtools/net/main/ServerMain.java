package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.common.reflect.ReflectDataClass;
import com.hongshaohua.jtools.tcp.defaults.DefaultTcpHandlerMap;
import com.hongshaohua.jtools.tcp.defaults.DefaultTcpHandlerMapInitializer;
import com.hongshaohua.jtools.tcp.defaults.DefaultTcpHandlerMsg;
import com.hongshaohua.jtools.tcp.server.TcpServer;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aska on 2018/2/12.
 */
public class ServerMain {

    public static void main(String[] args )
    {
        DOMConfigurator.configure("log4j.xml");
        try {
            TcpServer tcpServer = newTcpServer();

            //new DefaultAutoW(1);
            //new ReflectDataClass(Test.class);
            //new TestMsgDeserializer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ReflectDataClass clazz = new ReflectDataClass(Test.class);
            Test test = new Test();
            test.setBl(true);
            test.setB((byte)1);
            test.setC((char)2);
            test.setS((short)3);
            test.setI(4);
            test.setL(5);
            test.setF(6);
            test.setD(7);
            test.setStr("str");
            ArrayList<String> strList = new ArrayList<>();
            strList.add("strList1");
            strList.add("strList2");
            test.setStrList(strList);
            test.setStrList(null);

            //ByteBuf buf = DefaultTcpMsgSerializer.serialize(test, clazz);

            //Object tt = DefaultTcpMsgDeserializer.deserialize(buf, clazz);

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println( "Hello World!" );
    }

    private static TcpServer newTcpServer() {
        int boss = 1;
        int work = 4;
        MyHandler handler = new MyHandler(1, "server");
        List<DefaultTcpHandlerMsg> recvHandler = new ArrayList<>();
        recvHandler.add(handler);
        List<DefaultTcpHandlerMsg> sendHandler = new ArrayList<>();

        DefaultTcpHandlerMap handlerMap = new DefaultTcpHandlerMap(new MyListener("server"), recvHandler, sendHandler);


        TcpServer tcpServer = new TcpServer(boss, work, null);

        tcpServer.bind(10910, new DefaultTcpHandlerMapInitializer(handlerMap));
        return tcpServer;
    }
}
