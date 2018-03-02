package me.gavin.app.contact;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 好友请求
 *
 * @author gavin.xiong 2018/3/2
 */
@Entity
public class Request {

    @Id(autoincrement = true)
    private Long _id;
    private long uid;
    @SerializedName(value = "name", alternate = "account")
    private String name;
    @SerializedName(value = "avatar", alternate = "headImg")
    private String avatar;

    @Generated(hash = 2031639567)
    public Request(Long _id, long uid, String name, String avatar) {
        this._id = _id;
        this.uid = uid;
        this.name = name;
        this.avatar = avatar;
    }

    @Generated(hash = 1202777155)
    public Request() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getUid() {
        return this.uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Request{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
