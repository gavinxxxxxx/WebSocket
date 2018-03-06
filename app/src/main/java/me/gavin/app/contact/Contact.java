package me.gavin.app.contact;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 联系人 - 用户 | 群组 | 通知 | 功能账号
 *
 * @author gavin.xiong 2018/2/6
 */
@Entity
public class Contact {

    @Id(autoincrement = true)
    @Expose(serialize = false, deserialize = false)
    private Long _id; // id - 主键 自增 无意义
    @SerializedName(value = "id", alternate = {"userId", "friendId"})
    private long id;
    @Unique
    @SerializedName(value = "account")
    private String account;
    @SerializedName(value = "name")
    private String name;
    @SerializedName(value = "nick", alternate = "nickName")
    private String nick;
    @SerializedName(value = "avatar", alternate = {"headImg", "friendHeadImg"})
    private String avatar;
    @SerializedName(value = "type")
    private int type;

    @Generated(hash = 154205768)
    public Contact(Long _id, long id, String account, String name, String nick,
                   String avatar, int type) {
        this._id = _id;
        this.id = id;
        this.account = account;
        this.name = name;
        this.nick = nick;
        this.avatar = avatar;
        this.type = type;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
