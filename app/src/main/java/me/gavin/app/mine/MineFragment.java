package me.gavin.app.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.base.App;
import me.gavin.base.BindingFragment;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.FragmentMineBinding;

/**
 * 我的
 *
 * @author gavin.xiong 2018/2/26
 */
public class MineFragment extends BindingFragment<FragmentMineBinding> {

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.btnLogout.setOnClickListener(v -> {
            App.setUser(null);
            _mActivity.finish();
        });
    }
}
