package me.gavin.service;

import android.icu.text.Collator;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import me.gavin.app.contact.Contact;
import me.gavin.app.contact.Request;
import me.gavin.base.App;
import me.gavin.base.RxTransformers;
import me.gavin.db.dao.ContactDao;
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
    public Observable<List<Request>> getRequests() {
        return Observable.just(getDaoSession().getRequestDao().loadAll());
    }

    @Override
    public Observable<Result> applyRequest(long fid) {
        return getApi().passContact(fid);
    }

    @Override
    public Observable<Contact> getContact(long id) {
        List<Contact> list = getDaoSession().getContactDao()
                .queryBuilder()
                .where(ContactDao.Properties.Id.eq(id))
                .limit(1)
                .list();
        if (list == null || list.isEmpty()) {
            return Observable.error(new NullPointerException("获取用户信息错误 - 用户不存在"));
        }
        return Observable.just(list.get(0));
    }

    @Override
    public Observable<List<Contact>> getContacts() {
        return getApi().getContacts()
                .compose(RxTransformers.filterResultCD())
                .map(contacts -> { // 自己
                    contacts.add(0, App.getUser());
                    return contacts;
                })
                .map(contacts -> { // 系统
                    String json = AssetsUtils.readText(App.get(), "contacts_system.json");
                    List<Contact> list = getGson().fromJson(json, new TypeToken<ArrayList<Contact>>() {}.getType());
                    contacts.addAll(0, list);
                    return contacts;
                })
                .map(contacts -> {
                    String json = AssetsUtils.readText(App.get(), "contacts_hero.json");
                    List<Contact> list = getGson().fromJson(json, new TypeToken<ArrayList<Contact>>() {}.getType());
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setId(i + 10000L);
                    }
                    contacts.addAll(list);
                    return contacts;
                })
                .map(contacts -> {
                    getDaoSession().getContactDao().deleteAll();
                    getDaoSession().getContactDao().insertInTx(contacts);
                    return getDaoSession().getContactDao().loadAll();
                })
                .flatMap(Observable::fromIterable)
                .toSortedList((o1, o2) -> o1.getType() != o2.getType()
                        ? o2.getType() - o1.getType()
                        : Collator.getInstance(Locale.CHINESE).compare(o1.getName(), o2.getName()))
                .toObservable();
    }
}
