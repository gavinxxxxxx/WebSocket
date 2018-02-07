package me.gavin.service;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.contact.Contact;
import me.gavin.base.App;
import me.gavin.base.RxTransformers;
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
    public Observable<List<Contact>> getContacts() {
        return Observable.just("contacts.json")
                .map(s -> AssetsUtils.readText(App.get(), s))
                .map(s -> {
                    List<Contact> contacts = getGson().fromJson(s, new TypeToken<ArrayList<Contact>>() {}.getType());
                    getDaoSession().getContactDao().deleteAll();
                    getDaoSession().getContactDao().insertInTx(contacts);
                    return getDaoSession().getContactDao().loadAll();
                })
                .compose(RxTransformers.log());
    }
}
