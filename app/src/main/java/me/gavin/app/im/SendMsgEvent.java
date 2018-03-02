package me.gavin.app.im;

import me.gavin.app.message.Message;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/3/1
 */
public class SendMsgEvent {

    public Message message;

    public SendMsgEvent(Message message) {
        this.message = message;
    }
}
