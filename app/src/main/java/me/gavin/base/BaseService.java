//package me.gavin.base;
//
//import android.app.Service;
//
//import com.google.gson.Gson;
//
//import javax.inject.Inject;
//
//import dagger.Lazy;
//import io.reactivex.disposables.CompositeDisposable;
//import me.gavin.inject.component.ApplicationComponent;
//import okhttp3.OkHttpClient;
//
///**
// * 这里是萌萌哒注释君
// *
// * @author gavin.xiong 2018/3/2
// */
//public abstract class BaseService extends Service {
//
//    @Inject
//    protected Lazy<OkHttpClient> mOkHttpClient;
//    @Inject
//    protected Lazy<Gson> mGson;
//    @Inject
//    protected CompositeDisposable mCompositeDisposable;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        ApplicationComponent.Instance.get().inject(this);
//        afterCreate();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mCompositeDisposable.dispose();
//    }
//
//    protected abstract void afterCreate();
//}
