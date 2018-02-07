package me.gavin.service;

import io.reactivex.Observable;
import me.gavin.app.account.User;
import me.gavin.net.Result;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;

/**
 * MessageManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class AccountManager extends BaseManager implements DataLayer.AccountService {

    @Override
    public Observable<Result<Object>> register(String account, String pwd) {
        return getApi().register(account, pwd);
    }

    @Override
    public Observable<Result<User>> login(String account, String pwd) {
        return getApi().login(account, pwd);
    }

    @Override
    public Observable<Result<User>> getUserInfo(String account) {
        return getApi().getUserInfo(account);
    }
}
