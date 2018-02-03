package me.gavin.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.base.BindingFragment;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.FragmentMainBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/2/3
 */
public class MainFragment extends BindingFragment<FragmentMainBinding, MainViewModel> {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new MainViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {

    }
}
