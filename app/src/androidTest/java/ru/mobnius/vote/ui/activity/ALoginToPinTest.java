package ru.mobnius.vote.ui.activity;


import android.content.Intent;
import android.text.InputType;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobnius.vote.R;
import ru.mobnius.vote.utils.AuthUtil;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class ALoginToPinTest extends BaseActivityTest {
    private final static String TOO_SHORT = "18";
    private final static String LONG_ENOUGH = "1801";



    @After
    public void tearDown() {

    }

    @Test
    public void noPinTest() {

        //Проверка TextView с версией и Toast с версией

        ViewInteraction etLogin = onView(withId(R.id.auth_login));
        etLogin.perform(closeSoftKeyboard());
        onView(withId(R.id.auth_version)).check(matches(withText(containsString("Версия:")))).perform(click());
        onView(withText(containsString("Версия приложения")))
               .inRoot(withDecorView(not(is(loginTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //Поле логина. Очистка на случай если вставлен ник
        etLogin.perform(scrollTo(), clearText());
        ViewInteraction ibLoginClear = onView(withId(R.id.auth_login_clear));
        etLogin.perform(typeText(TOO_SHORT), closeSoftKeyboard());
        etLogin.check(matches(hasErrorText(AuthUtil.minLength(TOO_SHORT))));
        ibLoginClear.check(matches(isDisplayed()));
        ibLoginClear.perform(click());
        etLogin.check(matches(withText("")));
        etLogin.perform(typeText(LONG_ENOUGH));

        //Поле пароля.
        ViewInteraction etPassword = onView(withId(R.id.auth_password));
        ViewInteraction ibPasswordClear = onView(withId(R.id.auth_password_clear));
        ViewInteraction ibPasswordShow = onView(withId(R.id.auth_password_show));
        etPassword.perform(scrollTo(), typeText("12"));
        etPassword.check(matches(hasErrorText(AuthUtil.minLength(TOO_SHORT))));
        ibPasswordClear.check(matches(isDisplayed()));
        ibPasswordShow.check(matches(isDisplayed()));
        ibPasswordShow.perform(click());
        etPassword.check(matches(withInputType(InputType.TYPE_CLASS_NUMBER)));
        ibPasswordClear.perform(click());
        etPassword.check(matches(withText("")));
        etPassword.perform(typeText(LONG_ENOUGH));

        //Кнопка "Войти"
        ViewInteraction btnSignIn = onView(withId(R.id.auth_sign_in));
        btnSignIn.perform(scrollTo(), click());
        if (isServerUnavailable()) {
           onView(anyOf(withText("Сервер не доступен"), withText(containsString("У приложения отсутствует доступ к серверу"))))
                    .inRoot(withDecorView(not(is(loginTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        } else {
            onView(withText("Логин или пароль введены не верно."))
                    .inRoot(withDecorView(not(is(loginTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            etLogin.perform(replaceText(LOGIN));
            etPassword.perform(replaceText(PASSWORD));
            btnSignIn.perform(click());
            boolean noLocation = false;
            try {
                onView(anyOf(withText("Изменить режим"), withText("Включить геолокацию"))).perform(waitUntil(isDisplayed()));
                noLocation = true;
            } catch (NoMatchingViewException e) {
                e.printStackTrace();
            }
            if (noLocation) {
                return;
            }
            try {
                onView(withId(R.id.statistic_close)).perform(click());
            } catch (NoMatchingViewException e) {
                e.printStackTrace();
            }
            //Успешная авторизация
        }
    }
}

