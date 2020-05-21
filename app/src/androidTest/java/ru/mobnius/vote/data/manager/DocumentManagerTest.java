package ru.mobnius.vote.data.manager;

import android.location.Location;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.vote.VoteManager;
import ru.mobnius.vote.data.storage.models.PointTypes;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.ui.data.OnVoteListener;

import static org.junit.Assert.*;

public class DocumentManagerTest extends ManagerGenerate implements OnVoteListener {
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

        mDocumentManager = new DocumentManager(this);
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
    public void createResultTest() {
        String userPointId = UUID.randomUUID().toString();
        String notice = "";

        String resultId = mDocumentManager.createResult(userPointId, 1, 1, notice, null, 0, true);
        Results result = getDaoSession().getResultsDao().load(resultId);

        assertEquals(result.fn_user_point, userPointId);
        assertEquals(result.c_notice, notice);
        assertTrue(result.b_warning);
    }

    @Test
    public void createUserPointTest() {
        PointTypes pointType = new PointTypes();
        pointType.id = (long)1;
        pointType.c_const = "STANDART";
        getDaoSession().getPointTypesDao().insert(pointType);

        String userPointId = mDocumentManager.createUserPoint(null, 0, 0, null);
        UserPoints userPoint = getDaoSession().getUserPointsDao().load(userPointId);

        assertNotNull(userPoint);
    }

    @Override
    public VoteManager getVoteManager() {
        return null;
    }

    @Override
    public String getRouteId() {
        return mRouteId;
    }

    @Override
    public String getPointId() {
        return mPointId;
    }

    @Override
    public void onLocationStatusChange(int status, double latitude, double longitude) {

    }

    @Override
    public Location getCurrentLocation() {
        return new Location("");
    }
}