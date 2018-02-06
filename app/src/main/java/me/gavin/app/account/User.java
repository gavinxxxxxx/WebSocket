package me.gavin.app.account;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户
 *
 * @author gavin.xiong 2018/2/6
 */
@Entity
public class User {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String nick;
    private String avatar;

    @Generated(hash = 1909989624)
    public User(Long id, String name, String nick, String avatar) {
        this.id = id;
        this.name = name;
        this.nick = nick;
        this.avatar = avatar;
    }

    @Generated(hash = 586692638)
    public User() {
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


}
