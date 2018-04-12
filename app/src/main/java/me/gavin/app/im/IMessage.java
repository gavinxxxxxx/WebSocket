package me.gavin.app.im;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/3/1
 */
public class IMessage {

    private String type;
    private String content;
    private long from;
    private long to;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
