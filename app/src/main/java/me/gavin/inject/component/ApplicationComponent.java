package me.gavin.inject.component;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import me.gavin.app.im.IMService;
import me.gavin.base.BaseActivity;
import me.gavin.base.BaseFragment;
import me.gavin.base.BaseViewModel;
import me.gavin.inject.module.ApplicationModule;
import me.gavin.service.base.BaseManager;

/**
 * ApplicationComponent
 *
 * @author gavin.xiong 2017/4/28
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity activity);

    void inject(BaseFragment fragment);

    void inject(IMService service);

    void inject(BaseViewModel viewModel);

//    void inject(BaseDialogFragment dialogFragment);

    void inject(BaseManager manager);

    // 可以获取 ApplicationModule 及其 includes 的所有 Module 提供的对象（方法名随意）
    Application getApplication();

    Gson getGson();

    SharedPreferences getSharedPreferences();

    class Instance {

        private static ApplicationComponent sComponent;

        public static void set(@NonNull ApplicationComponent component) {
            sComponent = component;
        }

        public static ApplicationComponent get() {
            return sComponent;
        }
    }
}
