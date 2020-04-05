package ru.mobnius.vote.data.manager.synchronization;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.rpc.RPCItem;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.utils.PackageCreateUtils;
import ru.mobnius.vote.utils.PackageReadUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class MultipartUtilityTest {
    private final String URL_PART = "/synchronization/v0";
    private final BasicCredentials basicCredentials;

    public MultipartUtilityTest(){
        basicCredentials = new BasicCredentials("root", "root0");
    }

    @Test
    public void successTest() throws IOException {
        boolean ZIP = true;
        PackageCreateUtils packageCreateUtils = new PackageCreateUtils(ZIP);
        byte[] resultBytes = packageCreateUtils.addFrom(new RPCItem("shell.welcome", null)).generatePackage(UUID.randomUUID().toString());

        String text = new String(resultBytes);

        byte[] outputResultBytes;
        try {
            MultipartUtility multipartUtility = new MultipartUtility(ManagerGenerate.getBaseUrl() + URL_PART, basicCredentials);
            multipartUtility.addFilePart("synchronization", resultBytes);
            outputResultBytes = multipartUtility.finish();
            PackageReadUtils packageReadUtils = new PackageReadUtils(outputResultBytes, ZIP);
            RPCResult[] to = packageReadUtils.getResultTo(ZIP);
            assertEquals(to[0].result.records[0].getString("message"), "Hello");
            multipartUtility.destroy();
            packageReadUtils.destroy();
        }catch (Exception e) {
            fail();
        }

        packageCreateUtils.destroy();
    }

    @Test
    public void serverErrorTest() throws IOException {
        // обработка ошибки сервера
        PackageCreateUtils packageCreateUtils = new PackageCreateUtils(false);
        byte[] resultBytes = packageCreateUtils.addFrom(new RPCItem("server.error", null)).generatePackage(UUID.randomUUID().toString());
        MultipartUtility multipartUtility = null;
        try {
            multipartUtility = new MultipartUtility(ManagerGenerate.getBaseUrl() + URL_PART, basicCredentials);
            multipartUtility.addFilePart("synchronization", resultBytes);
            multipartUtility.finish();

            fail();
        }catch (Exception e){
            assertEquals("Статус код: 500. testing", e.getMessage());
        }
        assert multipartUtility != null;
        multipartUtility.destroy();
        packageCreateUtils.destroy();
    }
}
