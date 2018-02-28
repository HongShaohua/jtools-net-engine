package com.hongshaohua.jtools.net.main;

import com.hongshaohua.jtools.tcp.defaults.DefaultTcpMsg;

/**
 * Created by Aska on 2018/2/6.
 */
public class MyMsg extends DefaultTcpMsg {

    private int pic;

    public MyMsg() {
    }

    public MyMsg(int id, int pic) {
        super(id);
        this.pic = pic;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
