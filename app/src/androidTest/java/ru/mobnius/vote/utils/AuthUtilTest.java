package ru.mobnius.vote.utils;

import org.junit.Before;
import org.junit.Test;

import ru.mobnius.vote.DbGenerate;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.Version;

import static org.junit.Assert.*;

public class AuthUtilTest extends DbGenerate {
    private static final String INSPECTOR = "inspector";
    private static final String IN = "in";
    private static final String STATUS = "альфа-версия";

    @Before
    public void setUp() {
    }

    @Test
    public void minLength() {

        String firstResult = AuthUtil.minLength(INSPECTOR);
        assertEquals("", firstResult);

        String secondResult = AuthUtil.minLength(IN);
        assertTrue(secondResult.contains("Минимальная длина должна быть "));
    }

    @Test
    public void isButtonEnable() {
        boolean isEnabled = AuthUtil.isButtonEnable(INSPECTOR, "123");
        assertTrue(isEnabled);
        boolean isNotEnabled = !AuthUtil.isButtonEnable(INSPECTOR, "12");
        assertTrue(isNotEnabled);
    }

    @Test
    public void getVersionToast() {
        String versionName = VersionUtil.getVersionName(getContext());
        String status = "неизвестен";
        switch (new Version().getVersionParts(versionName)[2]) {
            case 0:
                status = getContext().getResources().getString(R.string.alphaText);
                break;
            case 1:
                status = getContext().getResources().getString(R.string.betaText);
                break;
            case 2:
                status = getContext().getResources().getString(R.string.releaseCandidateText);
                break;
            case 3:
                status = getContext().getResources().getString(R.string.productionText);
                break;
        }
        String rightVersion = AuthUtil.getVersionToast(getContext().getResources().getString(R.string.versionToast), versionName, status);
        assertNotNull(rightVersion);
    }
}