package ru.mobnius.vote;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;
import ru.mobnius.vote.data.storage.DbOpenHelper;
import ru.mobnius.vote.data.storage.models.DaoMaster;
import ru.mobnius.vote.data.storage.models.DaoSession;

/**
 * Вспомогательный класс для работы с БД
 */
public abstract class DbGenerate {
    private Context mContext;
    private DaoSession mDaoSession;

    public DbGenerate() {
        String dbName = getClass().getName();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDaoSession = new DaoMaster(new DbOpenHelper(mContext, dbName).getWritableDb()).newSession();

        // очистить все таблицы в БД можно еще вот так вот
        //DaoMaster.dropAllTables(getDaoSession("").getDatabase(), true);
        //DaoMaster.createAllTables(getDaoSession("").getDatabase(), true);
    }

    /**
     * получение ссылки на подключение к БД
     * @return объект DaoSession
     */
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public Context getContext() {
        return mContext;
    }
}
