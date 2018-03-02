package me.gavin.service;

import android.database.Cursor;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    public Observable<List<Message>> getMessage(int chatType, long chatId, int offset) {
        String sql = " SELECT * FROM MESSAGE LEFT JOIN CONTACT ON MESSAGE.SENDER = CONTACT._id WHERE MESSAGE.CHAT_TYPE = ? AND MESSAGE.CHAT_ID = ? ORDER BY MESSAGE.TIME DESC, MESSAGE._id DESC LIMIT 10 OFFSET " + offset;
        Cursor cursor = getDaoSession().getDatabase().rawQuery(sql, new String[]{String.valueOf(chatId), String.valueOf(chatType)});
        List<Message> result = format(cursor);
        Collections.reverse(result);
        return Observable.just(result);
    }

    @Override
    public void insert(Message message) {
        getDaoSession().getMessageDao().insert(message);
    }

    @Override
    public Observable<List<Message>> getChat() {
        String sql = " SELECT * FROM MESSAGE LEFT JOIN CONTACT ON MESSAGE.CHAT_ID = CONTACT._id WHERE MESSAGE.TIME IN ( SELECT MAX ( TIME ) FROM MESSAGE GROUP BY CHAT_TYPE , CHAT_ID ) GROUP BY CHAT_TYPE , CHAT_ID ORDER BY MESSAGE.TIME DESC ";
        Cursor cursor = getDaoSession().getDatabase().rawQuery(sql, null);
        return Observable.just(format(cursor));
    }

    private List<Message> format(@NotNull Cursor cursor) {
        List<Message> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Message msg = new Message(
                    null,
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
                    cursor.getInt(cursor.getColumnIndex(MessageDao.Properties.ChatType.columnName)),
                    cursor.getLong(cursor.getColumnIndex(MessageDao.Properties.ChatId.columnName)),
                    cursor.getString(cursor.getColumnIndex(MessageDao.Properties.Extra.columnName))
            );
            msg.setName(cursor.getString(cursor.getColumnIndex(ContactDao.Properties.Name.columnName)));
            msg.setAvatar(cursor.getString(cursor.getColumnIndex(ContactDao.Properties.Avatar.columnName)));
            result.add(msg);
        }
        cursor.close();
        return result;
    }
}
