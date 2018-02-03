package me.gavin.inject.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gavin.db.dao.DaoMaster;
import me.gavin.db.dao.DaoSession;
import me.gavin.util.L;

/**
 * DatabaseModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class DatabaseModule {

    @Singleton
    @Provides
    public DaoMaster provideDaoMaster(Application application) {
        L.e("provideDaoMaster");
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(application, "dbname", null);
        return new DaoMaster(devOpenHelper.getWritableDatabase());
    }

    @Singleton
    @Provides
    public DaoSession provideDaoSession(DaoMaster daoMaster) {
        L.e("provideDaoSession");
        return daoMaster.newSession();
    }
}
