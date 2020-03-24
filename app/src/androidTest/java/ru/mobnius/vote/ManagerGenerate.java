package ru.mobnius.vote;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.credentials.BasicUser;

public abstract class ManagerGenerate extends DbGenerate {
    private DataManager mDataManager;
    private FileManager mFileManager;
    private BasicCredentials mCredentials;

    public ManagerGenerate() {
        super();

        mDataManager = DataManager.createInstance(getDaoSession());
        mCredentials = getCredentials();
        mFileManager = FileManager.createInstance(mCredentials, getContext());
    }


    public DataManager getDataManager() {
        return mDataManager;
    }

    public FileManager getFileManager() {
        return mFileManager;
    }

    public static String getBaseUrl() {
        return MobniusApplication.getBaseUrl();
    }

    public static BasicCredentials getCredentials() {
        return new BasicCredentials("inspector", "inspector0");
    }

    public static BasicUser getBasicUser() {
        return new BasicUser(getCredentials(), 4, ".inspector.");
    }
}
