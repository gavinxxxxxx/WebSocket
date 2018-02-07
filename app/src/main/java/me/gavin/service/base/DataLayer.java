package me.gavin.service.base;

import com.google.gson.JsonArray;

import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.account.User;
import me.gavin.app.contact.Contact;
import me.gavin.app.message.Message;
import me.gavin.net.Result;
import okhttp3.ResponseBody;

/**
 * DataLayer
 *
 * @author gavin.xiong 2017/4/28
 */
public class DataLayer {

    private MessageService mMessageService;
    private ContactService mContactService;
    private AccountService mAccountService;
    private SettingService mSettingService;

    public DataLayer(
            MessageService messageService,
            ContactService contactService,
            AccountService accountService,
            SettingService settingService) {
        mMessageService = messageService;
        mContactService = contactService;
        mAccountService = accountService;
        mSettingService = settingService;
    }

    public MessageService getMessageService() {
        return mMessageService;
    }

    public ContactService getContactService() {
        return mContactService;
    }

    public AccountService getAccountService() {
        return mAccountService;
    }

    public SettingService getSettingService() {
        return mSettingService;
    }

    public interface MessageService {
        Observable<List<Message>> getMessage(String chatId, int offset);
    }

    public interface ContactService {
        Observable<List<Contact>> getContacts();
    }

    public interface AccountService {
        Observable<Result<Object>> register(String account, String pwd);

        Observable<Result<User>> login(String account, String pwd);

        Observable<Result<User>> getUserInfo(String account);
    }

    public interface SettingService {
        Observable<ResponseBody> download(String url);

        Observable<JsonArray> getLicense();
    }
}
