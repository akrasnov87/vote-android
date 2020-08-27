package ru.mobnius.vote.ui.activity;

import android.content.Intent;
import android.view.Gravity;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;

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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class APinActivationTest extends BaseActivityTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {

    }

    @Test
    public void pinActivationTest() {
        BasicUser basicUser = Authorization.getInstance().getLastAuthUser();
        String pinCode = "";
        AuthorizationCache cache = new AuthorizationCache(getContext());
        if (basicUser != null) {
            pinCode = cache.readPin(basicUser.getCredentials().login);
        }
        if (!pinCode.isEmpty()) {
            onView(withText("Сбросить пин-код и авторизоаться через логин и пароль?")).check(matches(isDisplayed()));
            onView(withText("Да")).perform(click());
        }
        if (isServerUnavailable()) {
            return;
        }
        onView(withId(R.id.auth_login)).perform(replaceText(LOGIN));
        onView(withId(R.id.auth_password)).perform(replaceText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.auth_sign_in)).perform(click());
        onView(withId(R.id.mainMenu_Toolbar)).perform(waitUntil(isDisplayed()));
        //Открываем NavigationDrawer

        onView(withId(R.id.mainMenuDrawerLayout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withText(getContext().getResources().getString(R.string.settings))).perform(click());
        //Включаем активацию по пин-коду. Так как espresso плохо работет с PreferencesScreen нужен такой непонятный код
        // в случае если требуется предварительная прокрутка для отображения необходимого view-элемента
        onView(withId(androidx.preference.R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Пин-код")), click()));

        ViewInteraction btnOne = onView(withText("1"));
        ViewInteraction pinCodeLinLay = onView(withId(R.id.pinFragment_pclTop));
        pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
        btnOne.perform(click());
        pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_FILLED)));
        onView(withId(R.id.pin_clear)).perform(click());
        pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
        btnOne.perform(click()).perform(click()).perform(click()).perform(click());
       onView(withText(containsString("Подтвердите пин-код")))
               .inRoot(withDecorView(not(is(loginTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        btnOne.perform(click()).perform(click()).perform(click());
        onView(withText("2")).perform(click());
        onView(withText("Пин-коды не совпадают, порпобуйте еще раз"))
               .inRoot(withDecorView(not(is(loginTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        btnOne.perform(click()).perform(click()).perform(click()).perform(click());
        onView(withText("Подтвердите пин-код"))
                .inRoot(withDecorView(not(is(loginTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        btnOne.perform(click()).perform(click()).perform(click()).perform(click());
        onView(withText(containsString("Вход по пин-коду активирован")))
                .inRoot(withDecorView(not(is(loginTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        onView(withId(R.id.pin_forgot)).perform(click());

    }
}
