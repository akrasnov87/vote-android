package ru.mobnius.vote.utils;

import android.content.Context;

import java.util.Date;

import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.storage.models.Audits;
import ru.mobnius.vote.data.storage.models.DaoSession;

public class AuditUtils {

    public final static String ON_AUTH = "on auth";
    public final static String UN_AUTH = "un auth";

    /**
     * запись информации в базу данных
     * @param message сообщение
     * @param type тип сообщения
     */
    public static void write(Context context, String message, String type, Level level){
        DaoSession daoSession = ru.mobnius.vote.data.manager.DataManager.getInstance().getDaoSession();

        Audits audit = new Audits();
        audit.objectOperationType = ru.mobnius.vote.data.manager.DbOperationType.CREATED;
        audit.d_date = DateUtil.convertDateToString(new Date());
        audit.fn_user = Authorization.getInstance().getUser().getUserId();
        audit.c_type = type;
        audit.c_data = message;
        daoSession.getAuditsDao().insert(audit);
    }

    /**
     * Уровни важности
     */
    public enum Level {
        LOW,
        MEDIUM,
        HIGH
    }
}
