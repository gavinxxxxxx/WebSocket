package me.gavin.service.base;

import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.message.Message;
import me.gavin.app.setting.License;
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

        Observable<List<License>> getLicense();
    }

}
