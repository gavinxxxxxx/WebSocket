package me.gavin.app.message;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 消息
 *
 * @author gavin.xiong 2018/2/1.
 */
@Entity
public class Message {

    public static final int CONTENT_TYPE_TEXT = 0; // 文本
    public static final int CONTENT_TYPE_IMAGE = 1; // 图片
    public static final int CONTENT_TYPE_AUDIO = 2; // 语音

    public static final int CHAT_TYPE_SINGLE = 0; // 单聊
    public static final int CHAT_TYPE_GROUP = 1; // 群聊
    public static final int CHAT_TYPE_OFFICIAL = 10; // 官方账号 - 公众号
    public static final int CHAT_TYPE_SYSTEM = 20; // 系统 - 通知等

    public static final long SYSTEM_CONTACT_REQUEST = -1L; // 好友请求

    @Id(autoincrement = true)
    @Expose(serialize = false, deserialize = false)
    private Long _id; // id - 主键 自增 无意义
    @Unique
    private String id; // 消息 id - 发送时生成
    private String content; // 消息体
    private String url; // 消息链接 - 图片文件消息
    private int width; // 消息图片宽
    private int height; // 消息图片高
    private long length; // 消息长度 图片 & 文件：大小 语音：时间
    private int type; // 消息类型 0：文本 1：图片 2：语音 3：...
    private int state; // 消息读取状态 0：未读 1：已读
    private long time; // 消息时间
    private long sender; // 消息发送人 id
    private int chatType; // 会话类型 0：单聊 1：群聊 2：官方账号 3：...
    private long chatId; // 会话 id - 群聊为群 id - 存储时生成
    private String extra; // 拓展 - json 字符串

    @Transient
    @Expose(serialize = false, deserialize = false)
    private String name; // 消息发送人名字
    @Transient
    @Expose(serialize = false, deserialize = false)
    private transient String avatar; // 消息发送人头像

    @Generated(hash = 1925179188)
    public Message(Long _id, String id, String content, String url, int width,
                   int height, long length, int type, int state, long time, long sender,
                   int chatType, long chatId, String extra) {
        this._id = _id;
        this.id = id;
        this.content = content;
        this.url = url;
        this.width = width;
        this.height = height;
        this.length = length;
        this.type = type;
        this.state = state;
        this.time = time;
        this.sender = sender;
        this.chatType = chatType;
        this.chatId = chatId;
        this.extra = extra;
    }

    @Generated(hash = 637306882)
    public Message() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getSender() {
        return this.sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public int getChatType() {
        return this.chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public long getChatId() {
        return this.chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", sender=" + sender +
                ", id=" + id +
                ", chatId=" + chatId +
                '}';
    }
}
