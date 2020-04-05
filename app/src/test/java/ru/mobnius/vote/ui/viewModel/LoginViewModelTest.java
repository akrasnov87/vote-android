package ru.mobnius.vote.ui.viewModel;

import org.junit.Before;
import org.junit.Test;

import ru.mobnius.vote.ui.model.LoginModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LoginViewModelTest {
    private LoginViewModel mSubject;

    @Before
    public void setUp() {
        mSubject = new LoginViewModel();
        String version = "4.3.0.520";
        mSubject.setModel(LoginModel.getInstance("login", "password", version));
    }

    @Test
    public void getVersionToast() {
        String result = mSubject.getVersionToast("%s.%s %s - %s", "4.2.0.540", "бета");
        assertEquals(result, "4.2 26.03.2020 09:00:00 - бета");

        result = mSubject.getVersionToast("%s.%s %s", "4.2.0.540", "бета");
        assertNull(result);
    }

    @Test
    public void isValidTemplate() {
        assertTrue(mSubject.isValidTemplate("%s.%s %s - %s"));
        assertTrue(mSubject.isValidTemplate(".%s.%s %s - %s."));
        assertTrue(mSubject.isValidTemplate(".%s.%s %s - %s"));
        assertTrue(mSubject.isValidTemplate("%s.%s %s - %s."));
    }

    @Test
    public void minLength() {
        assertFalse(mSubject.minLength("").isEmpty());
        assertTrue(mSubject.minLength("inspector").isEmpty());
    }
}
