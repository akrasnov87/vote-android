package ru.mobnius.vote.ui.viewModel;

import org.junit.Before;
import org.junit.Test;

import ru.mobnius.vote.ui.fragment.LoginFragment;
import ru.mobnius.vote.ui.model.LoginModel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoginViewModelTest {
    private LoginViewModel mSubject;
    private String mVersion = "4.3.0.520";

    @Before
    public void setUp() {
        mSubject = new LoginViewModel();
        mSubject.setModel(LoginModel.getInstance("login", "password", mVersion));
    }

    @Test
    public void getVersionToast() {
        String result = mSubject.getVersionToast("%s.%s %s - %s", "4.2.0.540", "бета");
        assertEquals(result, "4.2 18.12.2019 09:00:00 - бета");

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
