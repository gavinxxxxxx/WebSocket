package me.gavin.app.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.app.account.InputFragment;
import me.gavin.app.im.IMService;
import me.gavin.app.main.StartFragmentEvent;
import me.gavin.base.App;
import me.gavin.base.BindingFragment;
import me.gavin.base.RxBus;
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
        mBinding.setItem(App.getUser());

        mBinding.flAvatar.setOnClickListener(v -> {

        });
        mBinding.flNice.setOnClickListener(v ->
            RxBus.get().post(new StartFragmentEvent(InputFragment.newInstance(InputFragment.TYPE_NAME))));
        mBinding.flSignature.setOnClickListener(v ->
                RxBus.get().post(new StartFragmentEvent(InputFragment.newInstance(InputFragment.TYPE_SIGN))));

        mBinding.btnLogout.setOnClickListener(v -> {
            getActivity().stopService(new Intent(getActivity(), IMService.class));
            App.setUser(null);
            _mActivity.finish();
        });
    }
}
