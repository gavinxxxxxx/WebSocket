package me.gavin.inject.module;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gavin.app.im.IWebSocketListener;
import me.gavin.net.interceptor.HeaderInterceptor;
import okhttp3.OkHttpClient;

/**
 * ClientAPIModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class WebSocketModule {

    /**
     * OkHttp 客户端单例对象
     *
     * @return OkHttpClient
     */
    @Named("WebSocket")
    @Singleton
    @Provides
    OkHttpClient provideClientWS() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(new HeaderInterceptor())
                .build();
    }

    @Singleton
    @Provides
    IWebSocketListener provideWebSocketListener() {
        return new IWebSocketListener();
    }
}
