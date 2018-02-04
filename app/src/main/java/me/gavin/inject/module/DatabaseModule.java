package me.gavin.inject.module;

import android.app.Application;

import org.greenrobot.greendao.query.QueryBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gavin.base.Config;
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
    DaoMaster provideDaoMaster(Application application) {
        L.e("provideDaoMaster");
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(application, Config.DB_NAME);
        return new DaoMaster(devOpenHelper.getWritableDatabase());
    }

    @Singleton
    @Provides
    DaoSession provideDaoSession(DaoMaster daoMaster) {
        L.e("provideDaoSession");
        return daoMaster.newSession();
    }
}
