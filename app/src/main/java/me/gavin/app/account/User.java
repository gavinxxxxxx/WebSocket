package me.gavin.app.account;

import com.google.gson.annotations.SerializedName;

import me.gavin.app.contact.Contact;

/**
 * 用户
 *
 * @author gavin.xiong 2018/2/7
 */
public class User extends Contact {

    @SerializedName("remark")
    private String sign;
    @SerializedName("token")
    private String token;
    private boolean logged;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public String toString() {
        return "User{" +
                "logged=" + logged +
                ", token='" + token + '\'' +
                "} " + super.toString();
    }
}
