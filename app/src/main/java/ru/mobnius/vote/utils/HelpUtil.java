package ru.mobnius.vote.utils;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.storage.models.MobileHelp;
import ru.mobnius.vote.data.storage.models.MobileHelpDao;

public class HelpUtil {
    public static boolean isShow(String key) {
        if(key == null) {
            return false;
        }
        return DataManager.getInstance().getDaoSession().getMobileHelpDao().queryBuilder().where(MobileHelpDao.Properties.C_key.eq(key)).count() > 0;
    }

    public static MobileHelp get(String key) {
        return DataManager.getInstance().getDaoSession().getMobileHelpDao().queryBuilder().where(MobileHelpDao.Properties.C_key.eq(key)).list().get(0);
    }
}
