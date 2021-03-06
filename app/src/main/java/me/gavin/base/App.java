package me.gavin.base;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import me.gavin.app.account.User;
import me.gavin.app.daemon.ScreenBroadcastReceiver;
import me.gavin.inject.component.ApplicationComponent;
import me.gavin.inject.component.DaggerApplicationComponent;
import me.gavin.inject.module.ApplicationModule;

/**
 * 自定义 Application
 *
 * @author gavin.xiong 2017/4/25
 */
public class App extends Application {

    private static User sUser;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        setUser(ApplicationComponent.Instance.get()
                .getGson().fromJson(ApplicationComponent.Instance.get()
                        .getSharedPreferences()
                        .getString(BundleKey.USER, ""), User.class));

//        ScreenBroadcastReceiver mScreenReceiver = new ScreenBroadcastReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        filter.addAction(Intent.ACTION_USER_PRESENT);
//        registerReceiver(mScreenReceiver, filter);
    }

    private void initDagger() {
        ApplicationComponent.Instance.set(DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build());
    }

    public static Application get() {
        return ApplicationComponent.Instance.get().getApplication();
    }

    public static User getUser() {
        return sUser;
    }

    public static void setUser(User user) {
        sUser = user;
        ApplicationComponent.Instance.get()
                .getSharedPreferences()
                .edit()
                .putString(BundleKey.USER, ApplicationComponent.Instance.get().getGson().toJson(user))
                .apply();
    }
}
