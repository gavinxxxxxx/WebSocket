package me.gavin.service.base;

import com.google.gson.JsonArray;

import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.message.Message;
import okhttp3.ResponseBody;

/**
 * DataLayer
 *
 * @author gavin.xiong 2017/4/28
 */
public class DataLayer {

    private MessageService mMessageService;
    private SettingService mSettingService;

    public DataLayer(MessageService messageService, SettingService settingService) {
        mMessageService = messageService;
        mSettingService = settingService;
    }

    public MessageService getMessageService() {
        return mMessageService;
    }

    public SettingService getSettingService() {
        return mSettingService;
    }

    public interface MessageService {
        Observable<List<Message>> getMessage(String chatId, int offset);
    }

    public interface SettingService {
        Observable<ResponseBody> download(String url);

        Observable<JsonArray> getLicense();
    }

}
