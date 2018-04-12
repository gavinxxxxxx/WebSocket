package me.gavin.app.im.event;

import me.gavin.app.message.Message;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/3/1
 */
public class ReceiveMsgEvent {

    public Message message;

    public ReceiveMsgEvent(Message message) {
        this.message = message;
    }
}
