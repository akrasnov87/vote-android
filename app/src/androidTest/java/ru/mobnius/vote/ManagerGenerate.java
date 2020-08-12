package ru.mobnius.vote;

import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.credentials.BasicUser;

public abstract class ManagerGenerate extends DbGenerate {
    protected ManagerGenerate() {
        super();

        DataManager.createInstance(getDaoSession());
    }

    public static String getBaseUrl() {
        return MobniusApplication.getBaseUrl();
    }

    public static BasicCredentials getCredentials() {
        return new BasicCredentials("1801-01", "1801");
    }

    protected static BasicUser getBasicUser() {
        return new BasicUser(getCredentials(), 4, ".inspector.");
    }
}
