package ru.mobnius.vote.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.storage.models.Audits;

import static org.junit.Assert.*;

public class AuditUtilsTest extends ManagerGenerate {

    @Before
    public void setUp()  {
        Authorization.createInstance(getContext(), "").setUser(getBasicUser());
        PreferencesManager.createInstance(getContext(), getBasicUser().getCredentials().login);
        PreferencesManager.getInstance().setDebug(false);
        getDaoSession().getAuditsDao().deleteAll();
    }

    @Test
    public void write() {
        String txt = "test";
        AuditUtils.write(txt, AuditUtils.ON_AUTH, AuditUtils.Level.HIGH);

        List<Audits> audits = getDaoSession().getAuditsDao().loadAll();
        assertEquals(audits.size(), 1);

        Audits audit = audits.get(0);
        assertEquals(audit.c_data, txt);
        assertEquals(audit.c_type, AuditUtils.ON_AUTH);
        getDaoSession().getAuditsDao().deleteAll();

        AuditUtils.write(txt, AuditUtils.ON_AUTH, AuditUtils.Level.LOW);
        audits = getDaoSession().getAuditsDao().loadAll();
        assertEquals(audits.size(), 0);
        getDaoSession().getAuditsDao().deleteAll();

        PreferencesManager.getInstance().setDebug(true);
        AuditUtils.write(txt, AuditUtils.ON_AUTH, AuditUtils.Level.LOW);
        audits = getDaoSession().getAuditsDao().loadAll();
        assertEquals(audits.size(), 1);
    }
}