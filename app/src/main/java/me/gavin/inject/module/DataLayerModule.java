package me.gavin.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gavin.service.MessageManager;
import me.gavin.service.SettingManager;
import me.gavin.service.base.DataLayer;

/**
 * DataLayerModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class DataLayerModule {

    @Singleton
    @Provides
    MessageManager provideMessageManager() {
        return new MessageManager();
    }

    @Singleton
    @Provides
    SettingManager provideSettingManager() {
        return new SettingManager();
    }

    @Singleton
    @Provides
    DataLayer provideDataLayer(MessageManager messageManager, SettingManager settingManager) {
        return new DataLayer(messageManager, settingManager);
    }
}
