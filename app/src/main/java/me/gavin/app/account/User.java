package me.gavin.app.account;

import me.gavin.app.contact.Contact;

/**
 * 用户
 *
 * @author gavin.xiong 2018/2/7
 */
public class User extends Contact {

    private boolean logged;
    private String token;

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "logged=" + logged +
                ", token='" + token + '\'' +
                "} " + super.toString();
    }
}
