package me.gavin.service;

import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.message.Message;
import me.gavin.db.dao.MessageDao;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;

/**
 * MessageManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class MessageManager extends BaseManager implements DataLayer.MessageService {

    @Override
    public Observable<List<Message>> getMessage(long chatId, int offset) {
        return Observable.just(getDaoSession().getMessageDao()
                .queryBuilder()
                .whereOr(MessageDao.Properties.From.eq(chatId), MessageDao.Properties.To.eq(chatId))
                .limit(30)
                .offset(offset)
                .build()
                .list());
    }

    @Override
    public void insert(Message message) {
        getDaoSession().getMessageDao().insert(message);
    }

    @Override
    public Observable<List<Message>> getChat() {
        return Observable.just(getDaoSession().getMessageDao()
                .queryBuilder()
                .list());
    }
}
