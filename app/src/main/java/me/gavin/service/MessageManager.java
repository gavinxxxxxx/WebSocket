package me.gavin.service;

import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.message.Message;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;

/**
 * MessageManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class MessageManager extends BaseManager implements DataLayer.MessageService {

    @Override
    public Observable<List<Message>> getMessage(String chatId, int offset) {
        return Observable.just(getDaoSession().getMessageDao().loadAll());
    }
}
