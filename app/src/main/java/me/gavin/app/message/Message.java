package me.gavin.app.message;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 消息
 *
 * @author gavin.xiong 2018/2/1.
 */
@Entity
public class Message {

    private String id; // 消息 id
    private long from; // 消息发送人 id
    private long to; // 消息接收人id - 群聊为群 id
    private String name; // 消息发送人名字
    private String content; // 消息体
    private String url; // 消息链接 - 图片文件消息
    private int width; // 消息图片宽
    private int height; // 消息图片高
    private long length; // 消息长度 图片&文件：大小 语音：时间
    private int type; // 消息类型 0：文本 1：图片 2：语音 3：...
    private int attr; // 消息类型 0：单聊 1：群聊 2：...
    private int state; // 已读状态 0：未读 1：已读
    private long time; // 消息时间


    @Generated(hash = 1937749288)
    public Message(String id, long from, long to, String name, String content,
                   String url, int width, int height, long length, int type, int attr,
                   int state, long time) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.name = name;
        this.content = content;
        this.url = url;
        this.width = width;
        this.height = height;
        this.length = length;
        this.type = type;
        this.attr = attr;
        this.state = state;
        this.time = time;
    }

    @Generated(hash = 637306882)
    public Message() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAttr() {
        return attr;
    }

    public void setAttr(int attr) {
        this.attr = attr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
