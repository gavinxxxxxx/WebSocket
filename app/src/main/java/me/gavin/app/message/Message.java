package me.gavin.app.message;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 这里是萌萌哒注释菌
 *
 * @author gavin.xiong 2018/2/1.
 */
@Entity
public class Message {

    private String id;
    private String from;
    private String to;
    private String name;
    private String content;
    private String url;
    private int width;
    private int height;
    private int type;
    private int state;
    private long time;

    @Generated(hash = 771535110)
    public Message(String id, String from, String to, String name, String content,
            String url, int width, int height, int type, int state, long time) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.name = name;
        this.content = content;
        this.url = url;
        this.width = width;
        this.height = height;
        this.type = type;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
