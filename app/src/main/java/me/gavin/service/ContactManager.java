package me.gavin.service;

import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.contact.Contact;
import me.gavin.app.contact.Request;
import me.gavin.app.message.Message;
import me.gavin.base.App;
import me.gavin.base.RxTransformers;
import me.gavin.net.Result;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;
import me.gavin.util.L;

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
        Contact contact = getDaoSession().getContactDao().load(id);
        if (contact == null) {
            return Observable.error(new NullPointerException("获取用户信息错误 - 用户不存在"));
        }
        return Observable.just(contact);
    }

    @Override
    public Observable<List<Contact>> getContacts() {
        return getApi().getContacts()
                .compose(RxTransformers.filterResultCD())
                .map(contacts -> {
                    contacts.add(0, App.getUser());
                    contacts.add(0, new Contact(
                            Message.SYSTEM_CONTACT_REQUEST,
                            "好友申请",
                            "新的朋友",
                            "http://img2.3png.com/0c85b71fedcaca59bd4c60522d80fc527ae5.png"));

                    getDaoSession().getContactDao().deleteAll();
                    getDaoSession().getContactDao().insertInTx(contacts);
                    return getDaoSession().getContactDao().loadAll();
                });

//        return Observable.just("contacts.json")
//                .map(s -> AssetsUtils.readText(App.get(), s))
//                .map(s -> {
//                    List<Contact> contacts = getGson().fromJson(s, new TypeToken<ArrayList<Contact>>() {}.getType());
//                    for (int i = 0; i < contacts.size(); i++) {
//                        contacts.get(i).setId((long) i + 10000);
//                    }
//                    contacts.add(0, App.getUser());
//
//                    contacts.add(0, new Contact(
//                            Message.SYSTEM_CONTACT_REQUEST,
//                            "好友申请",
//                            "新的朋友",
//                            "http://img2.3png.com/0c85b71fedcaca59bd4c60522d80fc527ae5.png"));
//
//                    getDaoSession().getContactDao().deleteAll();
//                    getDaoSession().getContactDao().insertInTx(contacts);
//                    return getDaoSession().getContactDao().loadAll();
//                })
//                .compose(RxTransformers.log());
    }
}
