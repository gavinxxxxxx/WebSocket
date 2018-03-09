package me.gavin.net.interceptor;

import java.io.IOException;

import me.gavin.app.account.User;
import me.gavin.base.App;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/2/7
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (App.getUser() == null) return chain.proceed(chain.request());
        return chain.proceed(chain.request()
                .newBuilder()
                .header("A-SID", getToken())
                .build());
    }

    private String getToken() {
        User user = App.getUser();
        return user != null && user.getToken() != null
                ? user.getToken()
                : "";
    }
}
