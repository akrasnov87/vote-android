package ru.mobnius.vote.ui.activity;


import android.content.Intent;

import androidx.test.espresso.ViewInteraction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.ui.component.PinCodeLinLay;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
public class APinTest extends BaseActivityTest{




    @After
    public void tearDown() {

    }

    @Test
    public void pinTest() {
        BasicUser basicUser = Authorization.getInstance().getLastAuthUser();
        String pinCode = "";
        if (basicUser != null) {
            AuthorizationCache cache = new AuthorizationCache(getContext());
            pinCode = cache.readPin(basicUser.getCredentials().login);
        }
        if (pinCode.isEmpty()) {
            return;
        }
        int x = Integer.parseInt(String.valueOf(pinCode.charAt(0)));
        if (x == 9) {
            x--;
        } else {
            x++;
        }
        onView(withId(R.id.pin_forgot)).perform(click());
        onView(withText("Сбросить пин-код и авторизоаться через логин и пароль?")).check(matches(isDisplayed()));
        onView(withText("Нет")).perform(click());

        ViewInteraction pinCodeLinLay = onView(withId(R.id.pinFragment_pclTop));
        pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
        onView(withText(String.valueOf(x))).perform(click());
        onView(withText(String.valueOf(pinCode.charAt(1)))).perform(click());
        onView(withText(String.valueOf(pinCode.charAt(2)))).perform(click());
        onView(withText(String.valueOf(pinCode.charAt(3)))).perform(click());
        onView(withText("Неправильный пин"))
                .inRoot(withDecorView(not(is(loginTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(String.valueOf(pinCode.charAt(0)))).perform(click());
        pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_FILLED)));
        onView(withText(String.valueOf(pinCode.charAt(1)))).perform(click());
        onView(withText(String.valueOf(pinCode.charAt(2)))).perform(click());

        onView(withId(R.id.pin_forgot)).perform(click());
        onView(withText("Да")).perform(click());
    }

}
