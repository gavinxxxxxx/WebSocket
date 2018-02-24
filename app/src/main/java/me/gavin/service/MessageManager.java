package me.gavin.service;

import android.database.Cursor;

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
    public Observable<List<Message>> getMessage(long chatId, int chatType, int offset) {
        return Observable.just(getDaoSession().getMessageDao()
                .queryBuilder()
                .where(MessageDao.Properties.ChatId.eq(chatId), MessageDao.Properties.ChatType.eq(chatType))
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
        // String sql = " SELECT * FROM MESSAGE WHERE TIME IN ( SELECT MAX ( TIME ) FROM MESSAGE GROUP BY CHAT_ID , CHAT_TYPE ) ORDER BY TIME DESC ";
        StringBuilder sb = new StringBuilder(" WHERE ")
                .append(MessageDao.Properties.Time.columnName)
                .append(" IN ( SELECT MAX ( ")
                .append(MessageDao.Properties.Time.columnName)
                .append(" ) FROM ")
                .append(MessageDao.TABLENAME)
                .append(" GROUP BY ")
                .append(MessageDao.Properties.ChatId.columnName)
                .append(" , ")
                .append(MessageDao.Properties.ChatType.columnName)
                .append(" ) ORDER BY ")
                .append(MessageDao.Properties.Time.columnName)
                .append(" DESC ");
        return Observable.just(getDaoSession().getMessageDao().queryRaw(sb.toString()));
    }

    // @Override
    public Observable<List<Message>> getChat2() {
        Cursor cursor = getDaoSession().getDatabase().rawQuery("", null);
        while (cursor.moveToNext()) {
            // TODO: 2018/2/24
        }
        return null;
    }
}
