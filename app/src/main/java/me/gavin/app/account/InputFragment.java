package me.gavin.app.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import io.reactivex.Observable;
import me.gavin.base.App;
import me.gavin.base.BindingFragment;
import me.gavin.base.BundleKey;
import me.gavin.base.RxTransformers;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.FragmentInputBinding;
import me.gavin.net.Result;
import me.gavin.util.InputUtil;

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
        mBinding.includeToolbar.toolbar.setTitle(mPageType == TYPE_NAME ? "修改昵称" : "修改签名");
        mBinding.includeToolbar.toolbar.inflateMenu(R.menu.action_done);
        mBinding.includeToolbar.toolbar.setOnMenuItemClickListener(item -> {
            doUpdate();
            return false;
        });

        mBinding.editText.setText(mPageType == TYPE_NAME ? App.getUser().getNick() : App.getUser().getSign());
        InputUtil.show(getContext(), mBinding.editText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        InputUtil.hide(getContext(), mBinding.editText);
    }

    private Observable<Result> getObservable(String value) {
        return mPageType == TYPE_NAME
                ? getDataLayer().getAccountService().updateName(value)
                : getDataLayer().getAccountService().updateSign(value);
    }

    private void doUpdate() {
        String value = mBinding.editText.getText().toString().trim();
        getObservable(value)
                .compose(RxTransformers.applySchedulers())
                .subscribe(result -> {
                    if (result.isSuccess()) {
                        User user = App.getUser();
                        if (mPageType == TYPE_NAME) {
                            user.setNick(value);
                        } else {
                            user.setSign(value);
                        }
                        App.setUser(user);
                        pop();
                    } else {
                        Snackbar.make(mBinding.editText, result.getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                }, t -> Snackbar.make(mBinding.editText, t.getMessage(), Snackbar.LENGTH_LONG).show());
    }
}
