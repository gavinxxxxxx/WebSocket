package me.gavin.app.contact;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 联系人 - 用户 | 群组 | 通知 | 功能账号
 *
 * @author gavin.xiong 2018/2/6
 */
@Entity
public class Contact {

    @Id
    @SerializedName(value = "id", alternate = "userId")
    private Long id;
    @SerializedName(value = "name", alternate = "account")
    private String name;
    @SerializedName(value = "nick", alternate = "nickName")
    private String nick;
    @SerializedName(value = "avatar", alternate = "headImg")
    private String avatar;

    @Generated(hash = 668460499)
    public Contact(Long id, String name, String nick, String avatar) {
        this.id = id;
        this.name = name;
        this.nick = nick;
        this.avatar = avatar;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
