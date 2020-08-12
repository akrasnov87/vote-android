package ru.mobnius.vote.data.manager.exception;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.storage.models.ClientErrors;
import ru.mobnius.vote.data.storage.models.ClientErrorsDao;
import ru.mobnius.vote.data.storage.models.DaoSession;

public class ExceptionUtilsTest extends ManagerGenerate {
    private IFileExceptionManager fileExceptionManager;
    private DaoSession daoSession;

    @Before
    public void setUp() {
        Authorization.createInstance(getContext(), "");
        Authorization.getInstance().setUser(new BasicUser(getCredentials(), 1, ""));
        fileExceptionManager = FileExceptionManager.getInstance(getContext());
        daoSession = getDaoSession();
        fileExceptionManager.deleteFolder();
        daoSession.getClientErrorsDao().deleteAll();
    }

    @Test
    public void saveLocalExceptionTest() {
        ExceptionModel model = ExceptionModel.getInstance(new Date(), "Ошибка", IExceptionGroup.NONE, IExceptionCode.ALL);
        String str = model.toString();
        String fileName = model.getFileName();
        fileExceptionManager.writeBytes(fileName, str.getBytes());
        ExceptionUtils.saveLocalException(getContext(), -1, daoSession);

        List<ClientErrors> list = daoSession.getClientErrorsDao().queryBuilder().where(ClientErrorsDao.Properties.ObjectOperationType.eq(DbOperationType.CREATED)).list();
        Assert.assertEquals(list.size(), 1);
    }

    @Test
    public void saveExceptionTest(){
        ExceptionUtils.saveException(getContext(), daoSession, new Exception("тест"), IExceptionGroup.NONE, IExceptionCode.ALL);

        List<ClientErrors> list = daoSession.getClientErrorsDao().queryBuilder().where(ClientErrorsDao.Properties.ObjectOperationType.eq(DbOperationType.CREATED)).list();
        Assert.assertEquals(list.size(), 1);
    }

    @Test
    public void codeToStringTest(){
        Assert.assertEquals(ExceptionUtils.codeToString(2), "002");
        Assert.assertEquals(ExceptionUtils.codeToString(21), "021");
        Assert.assertEquals(ExceptionUtils.codeToString(215), "215");
        Assert.assertEquals(ExceptionUtils.codeToString(2158), "2158");
    }
}
