package me.gavin.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.gavin.base.BindingFragment;
import me.gavin.base.RxTransformers;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.FragmentMainBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/2/3
 */
public class MainFragment extends BindingFragment<FragmentMainBinding> {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        initViewPager();

        Observable.timer(5, TimeUnit.SECONDS)
                .flatMap(arg0 -> getDataLayer()
                        .getMessageService()
                        .getMessage("", 0))
                .compose(RxTransformers.log())
                .flatMap(messages -> getDataLayer()
                        .getSettingService()
                        .getLicense())
                .compose(RxTransformers.log())
                .compose(RxTransformers.applySchedulers())
                .subscribe();
    }

    private void initViewPager() {
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        mBinding.viewPager.setAdapter(pagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(3);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

}
