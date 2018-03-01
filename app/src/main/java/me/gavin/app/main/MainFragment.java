package me.gavin.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.app.im.IMService;
import me.gavin.base.BindingFragment;
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
        getActivity().startService(new Intent(getActivity(), IMService.class));
        initViewPager();
    }

    private void initViewPager() {
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        mBinding.viewPager.setAdapter(pagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(3);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }
}
