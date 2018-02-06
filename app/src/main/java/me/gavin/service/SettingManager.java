package me.gavin.service;

import com.google.gson.JsonArray;

import io.reactivex.Observable;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;
import okhttp3.ResponseBody;

/**
 * SettingManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class SettingManager extends BaseManager implements DataLayer.SettingService {

    @Override
    public Observable<ResponseBody> download(String url) {
        return getApi().download(url);
    }

    @Override
    public Observable<JsonArray> getLicense() {
        return getApi().getLicense();
    }
}
