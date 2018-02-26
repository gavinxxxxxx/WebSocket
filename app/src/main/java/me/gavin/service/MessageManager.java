package me.gavin.service;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.message.Message;
import me.gavin.db.dao.ContactDao;
import me.gavin.db.dao.MessageDao;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;

/**
 * MessageManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class MessageManager extends BaseManager implements DataLayer.MessageService {

//    @Override
//    public Observable<List<Message>> getMessage(long chatId, int chatType, int offset) {
//        return Observable.just(getDaoSession().getMessageDao()
//                .queryBuilder()
//                .where(MessageDao.Properties.ChatId.eq(chatId), MessageDao.Properties.ChatType.eq(chatType))
//                .limit(30)
//                .offset(offset)
//                .build()
//                .list());
//    }

    @Override
    public Observable<List<Message>> getMessage(long chatId, int chatType, int offset) {
        String sql = " SELECT * FROM MESSAGE LEFT JOIN CONTACT ON MESSAGE.SENDER = CONTACT._id WHERE MESSAGE.CHAT_ID = ? AND MESSAGE.CHAT_TYPE = ? LIMIT 30 OFFSET " + offset;
        Cursor cursor = getDaoSession().getDatabase().rawQuery(sql, new String[]{String.valueOf(chatId), String.valueOf(chatType)});
        List<Message> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Message message = new Message(
                    cursor.getString(cursor.getColumnIndex(MessageDao.Properties.Id.columnName)),
                    cursor.getString(cursor.getColumnIndex(MessageDao.Properties.Content.columnName)),
                    cursor.getString(cursor.getColumnIndex(MessageDao.Properties.Url.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.Width.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.Height.columnName)),
                    cursor.getLong(cursor.getColumnIndex(MessageDao.Properties.Length.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.Type.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.State.columnName)),
                    cursor.getLong(cursor.getColumnIndex(MessageDao.Properties.Time.columnName)),
                    cursor.getLong(cursor.getColumnIndex(MessageDao.Properties.Sender.columnName)),
                    cursor.getLong(cursor.getColumnIndex(MessageDao.Properties.ChatId.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.ChatType.columnName))
            );
            message.setName(cursor.getString(cursor.getColumnIndex(ContactDao.Properties.Name.columnName)));
            message.setAvatar(cursor.getString(cursor.getColumnIndex(ContactDao.Properties.Avatar.columnName)));
            result.add(message);
        }
        cursor.close();
        return Observable.just(result);
    }

    @Override
    public void insert(Message message) {
        getDaoSession().getMessageDao().insert(message);
    }

//    @Override
//    public Observable<List<Message>> getChat() {
//        // String sql = " SELECT * FROM MESSAGE WHERE TIME IN ( SELECT MAX ( TIME ) FROM MESSAGE GROUP BY CHAT_ID , CHAT_TYPE ) ORDER BY TIME DESC ";
//        StringBuilder sb = new StringBuilder(" WHERE ")
//                .append(MessageDao.Properties.Time.columnName)
//                .append(" IN ( SELECT MAX ( ")
//                .append(MessageDao.Properties.Time.columnName)
//                .append(" ) FROM ")
//                .append(MessageDao.TABLENAME)
//                .append(" GROUP BY ")
//                .append(MessageDao.Properties.ChatId.columnName)
//                .append(" , ")
//                .append(MessageDao.Properties.ChatType.columnName)
//                .append(" ) ORDER BY ")
//                .append(MessageDao.Properties.Time.columnName)
//                .append(" DESC ");
//        return Observable.just(getDaoSession().getMessageDao().queryRaw(sb.toString()));
//    }

    @Override
    public Observable<List<Message>> getChat() {
        // String sql = " SELECT * FROM MESSAGE WHERE TIME IN ( SELECT MAX ( TIME ) FROM MESSAGE GROUP BY CHAT_ID , CHAT_TYPE ) ORDER BY TIME DESC ";
        String sql = " SELECT * FROM MESSAGE LEFT JOIN CONTACT ON MESSAGE.CHAT_ID = CONTACT._id WHERE MESSAGE.TIME IN ( SELECT MAX ( TIME ) FROM MESSAGE GROUP BY CHAT_ID , CHAT_TYPE ) ORDER BY MESSAGE.TIME DESC ";
        Cursor cursor = getDaoSession().getDatabase().rawQuery(sql, null);
        List<Message> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Message message = new Message(
                    cursor.getString(cursor.getColumnIndex(MessageDao.Properties.Id.columnName)),
                    cursor.getString(cursor.getColumnIndex(MessageDao.Properties.Content.columnName)),
                    cursor.getString(cursor.getColumnIndex(MessageDao.Properties.Url.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.Width.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.Height.columnName)),
                    cursor.getLong(cursor.getColumnIndex(MessageDao.Properties.Length.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.Type.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.State.columnName)),
                    cursor.getLong(cursor.getColumnIndex(MessageDao.Properties.Time.columnName)),
                    cursor.getLong(cursor.getColumnIndex(MessageDao.Properties.Sender.columnName)),
                    cursor.getLong(cursor.getColumnIndex(MessageDao.Properties.ChatId.columnName)),
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.ChatType.columnName))
            );
            message.setName(cursor.getString(cursor.getColumnIndex(ContactDao.Properties.Name.columnName)));
            message.setAvatar(cursor.getString(cursor.getColumnIndex(ContactDao.Properties.Avatar.columnName)));
            result.add(message);
        }
        cursor.close();
        return Observable.just(result);
    }
}
