package me.gavin.app.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import me.gavin.app.main.MainFragment;
import me.gavin.base.App;
import me.gavin.base.BindingFragment;
import me.gavin.base.RxTransformers;
import me.gavin.db.DBHelper;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.FragmentLoginBinding;
import me.gavin.util.MD5;
import me.yokeyword.fragmentation.Fragmentation;

/**
 * 登录
 *
 * @author gavin.xiong 2018/2/7
 */
public class LoginFragment extends BindingFragment<FragmentLoginBinding> {

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.btnLogin.setOnClickListener(v -> doLogin());
        mBinding.btnRegister.setOnClickListener(v -> doRegister());
    }

    private void doLogin() {
        Fragmentation.builder().debug(false);

        String account = mBinding.etAccount.getText().toString().trim();
        String pass = MD5.md5(mBinding.etPass.getText().toString().trim());
        getDataLayer().getAccountService()
                .login(account, pass)
                .compose(RxTransformers.filterResultCD())
                .flatMap(user -> {
                    App.setUser(user);
                    return getDataLayer().getAccountService()
                            .getUserInfo(account);
                })
                .compose(RxTransformers.filterResultCD())
                .compose(RxTransformers.applySchedulers())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(user -> {
                    user.setLogged(true);
                    user.setName(user.getNick());
                    App.setUser(user);
                    DBHelper.get().notifyDB();
                    startWithPop(MainFragment.newInstance());
                }, throwable -> {
                    throwable.printStackTrace();
                    Snackbar.make(mBinding.btnRegister, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                });
    }

    private void doRegister() {
        String account = mBinding.etAccount.getText().toString().trim();
        String pass = MD5.md5(mBinding.etPass.getText().toString().trim());
        getDataLayer().getAccountService()
                .register(account, pass)
                .compose(RxTransformers.filterResultC())
                .compose(RxTransformers.applySchedulers())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(result -> doLogin(),
                        throwable -> {
                            throwable.printStackTrace();
                            Snackbar.make(mBinding.btnRegister, throwable.toString(), Snackbar.LENGTH_LONG).show();
                        });
    }

}
