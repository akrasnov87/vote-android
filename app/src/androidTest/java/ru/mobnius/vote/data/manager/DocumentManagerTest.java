package ru.mobnius.vote.data.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.storage.models.PointTypes;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.ui.model.Meter;
import ru.mobnius.vote.utils.DateUtil;

import static org.junit.Assert.*;

public class DocumentManagerTest extends ManagerGenerate {
    private DocumentManager mDocumentManager;
    private FileManager fileManager;
    private String mPointId;
    private String mRouteId;

    @Before
    public void setUp() {
        mRouteId = UUID.randomUUID().toString();
        mPointId = UUID.randomUUID().toString();
        BasicUser basicUser = getBasicUser();
        Authorization.createInstance(getContext(), getBaseUrl()).setUser(basicUser);

        mDocumentManager = new DocumentManager(getDaoSession(), mRouteId, mPointId);
        fileManager = FileManager.createInstance(basicUser.getCredentials(), getContext());
    }

    @After
    public void tearDown() {
        getDaoSession().getResultsDao().deleteAll();
        getDaoSession().getUserPointsDao().deleteAll();
        getDaoSession().getPointTypesDao().deleteAll();
        fileManager.clearUserFolder();
    }

    @Test
    public void createResult() {
        long outputTypeId = 1;
        String userPointId = UUID.randomUUID().toString();
        String notice = "";
        boolean warning = true;

        String resultId = mDocumentManager.createResult(outputTypeId, userPointId, notice, null, warning);
        Results result = getDaoSession().getResultsDao().load(resultId);

        assertEquals(result.fn_type, outputTypeId);
        assertEquals(result.fn_user_point, userPointId);
        assertEquals(result.c_notice, notice);
        assertTrue(result.b_warning);
        notice = "Hello";
        warning = false;

        mDocumentManager.updateResult(resultId, notice, null, warning);
        assertEquals(result.c_notice, notice);
        assertFalse(result.b_warning);
    }

    @Test
    public void createUserPoint() {
        PointTypes pointType = new PointTypes();
        pointType.id = (long)1;
        pointType.c_const = "STANDART";
        getDaoSession().getPointTypesDao().insert(pointType);

        String userPointId = mDocumentManager.createUserPoint(null, null, 0, 0, null, false);
        UserPoints userPoint = getDaoSession().getUserPointsDao().load(userPointId);

        assertNotNull(userPoint);

        mDocumentManager.updateUserPoint(userPointId, "{\"tel\":\"\"}", "{\"email\":\"\"}", "{\"data\":\"\"}");
        userPoint = getDaoSession().getUserPointsDao().load(userPointId);
        assertEquals(userPoint.jb_data, "{\"data\":\"\"}");
        assertEquals(userPoint.jb_email, "{\"email\":\"\"}");
        assertEquals(userPoint.jb_tel, "{\"tel\":\"\"}");
    }
}