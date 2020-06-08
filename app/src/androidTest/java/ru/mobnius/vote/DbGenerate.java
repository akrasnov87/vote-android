package ru.mobnius.vote;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import ru.mobnius.vote.data.storage.DbOpenHelper;
import ru.mobnius.vote.data.storage.models.DaoMaster;
import ru.mobnius.vote.data.storage.models.DaoSession;

/**
 * Вспомогательный класс для работы с БД
 */
public abstract class DbGenerate {
    private final Context mContext;
    private final DaoSession mDaoSession;

    protected DbGenerate() {
        String dbName = getClass().getName();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDaoSession = new DaoMaster(new DbOpenHelper(mContext, dbName).getWritableDb()).newSession();
    }

    /**
     * получение ссылки на подключение к БД
     * @return объект DaoSession
     */
    protected DaoSession getDaoSession() {
        return mDaoSession;
    }

    protected Context getContext() {
        return mContext;
    }

}
