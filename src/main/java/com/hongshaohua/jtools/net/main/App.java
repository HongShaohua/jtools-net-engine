package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.common.reflect.ReflectDataClass;
import com.hongshaohua.jtools.tcp.server.TcpServer;
import com.hongshaohua.jtools.tcp.server.defaults.DefaultTcpHandlerMap;
import com.hongshaohua.jtools.tcp.server.defaults.DefaultTcpHandlerMapInitializer;
import com.hongshaohua.jtools.tcp.server.defaults.DefaultTcpHandlerMsg;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main(String[] args )
    {
        DOMConfigurator.configure("log4j.xml");
        try {
            int boss = 1;
            int work = 4;
            MyHandler handler = new MyHandler(1);
            List<DefaultTcpHandlerMsg> recvHandler = new ArrayList<>();
            recvHandler.add(handler);
            List<DefaultTcpHandlerMsg> sendHandler = new ArrayList<>();

            DefaultTcpHandlerMap handlerMap = new DefaultTcpHandlerMap(new MyListener(), recvHandler, sendHandler);


            TcpServer tcpServer = new TcpServer(boss, work, null);
            tcpServer.bind(10910, new DefaultTcpHandlerMapInitializer(handlerMap));

            //Thread.sleep(1000000000);

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
}
