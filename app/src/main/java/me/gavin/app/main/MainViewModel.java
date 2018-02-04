package me.gavin.app.main;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.gavin.base.FragViewModel;
import me.gavin.base.RxTransformers;
import me.gavin.im.ws.databinding.FragmentMainBinding;
import me.gavin.util.L;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/2/3
 */
public class MainViewModel extends FragViewModel<MainFragment, FragmentMainBinding> {

    public MainViewModel(Context context, MainFragment fragment, FragmentMainBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    public void afterCreate() {
        Observable.timer(5, TimeUnit.SECONDS)
                .flatMap(arg0 -> getDataLayer()
                        .getMessageService()
                        .getMessage("", 0))
                .map(arg0 -> {
                    L.e(arg0);
                    return arg0;
                })
                .flatMap(messages -> getDataLayer()
                        .getSettingService()
                        .getLicense())
                .compose(RxTransformers.applySchedulers())
                .subscribe(L::e, L::e);
    }
}
