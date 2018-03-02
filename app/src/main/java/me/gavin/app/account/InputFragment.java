package me.gavin.app.account;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.base.BindingFragment;
import me.gavin.base.BundleKey;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.FragmentInputBinding;

/**
 * 修改 昵称 | 签名
 *
 * @author gavin.xiong 2018/3/2
 */
public class InputFragment extends BindingFragment<FragmentInputBinding> {

    public static final int TYPE_NAME = 0; // 修改昵称
    public static final int TYPE_SIGN = 1; // 修改签名

    private int mPageType;

    public static InputFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(BundleKey.PAGE_TYPE, type);
        InputFragment fragment = new InputFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_input;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mPageType = getArguments().getInt(BundleKey.PAGE_TYPE);

        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        if (mPageType == TYPE_NAME) {
            mBinding.includeToolbar.toolbar.setTitle("修改昵称");
        } else if (mPageType == TYPE_SIGN) {
            mBinding.includeToolbar.toolbar.setTitle("修改签名");
        }
        mBinding.includeToolbar.toolbar.inflateMenu(R.menu.action_done);
        mBinding.includeToolbar.toolbar.setOnMenuItemClickListener(item -> {
            if (mPageType == TYPE_NAME) {

            } else if (mPageType == TYPE_SIGN) {

            }
            return false;
        });
    }
}
