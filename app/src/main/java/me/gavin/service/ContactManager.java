package me.gavin.service;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.contact.Contact;
import me.gavin.app.contact.Request;
import me.gavin.base.App;
import me.gavin.base.RxTransformers;
import me.gavin.net.Result;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;
import me.gavin.util.AssetsUtils;

/**
 * MessageManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class ContactManager extends BaseManager implements DataLayer.ContactService {

    @Override
    public Observable<Result<List<Contact>>> queryContact(String query) {
        return getApi().queryContact(query);
    }

    @Override
    public Observable<Result> applyContact(long fid) {
        return getApi().applyContact(fid);
    }

    @Override
    public void insetRequest(Request request) {
        getDaoSession().getRequestDao()
                .insert(request);
    }

    @Override
    public Observable<Result> dearContact(long fid, boolean apply) {
        return apply ? getApi().passContact(fid) : getApi().refuseContact(fid);
    }

    @Override
    public Observable<Contact> getContact(long id) {
        Contact contact = getDaoSession().getContactDao().load(id);
        if (contact == null) {
            return Observable.error(new NullPointerException("获取用户信息错误 - 用户不存在"));
        }
        return Observable.just(contact);
    }

    @Override
    public Observable<List<Contact>> getContacts() {
        return Observable.just("contacts.json")
                .map(s -> AssetsUtils.readText(App.get(), s))
                .map(s -> {
                    List<Contact> contacts = getGson().fromJson(s, new TypeToken<ArrayList<Contact>>() {}.getType());
                    for (int i = 0; i < contacts.size(); i++) {
                        contacts.get(i).setId((long) i + 10000);
                    }
                    contacts.add(0, App.getUser());
                    getDaoSession().getContactDao().deleteAll();
                    getDaoSession().getContactDao().insertInTx(contacts);
                    return getDaoSession().getContactDao().loadAll();
                })
                .compose(RxTransformers.log());
    }
}
