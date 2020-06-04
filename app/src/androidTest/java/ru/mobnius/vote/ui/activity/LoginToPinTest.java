package ru.mobnius.vote.ui.activity;


import android.content.Intent;
import android.text.InputType;
import android.view.View;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.ui.component.PinCodeLinLay;
import ru.mobnius.vote.utils.AuthUtil;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
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
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginToPinTest extends ManagerGenerate {
    private final static String TOO_SHORT = "in";
    private final static String LONG_ENOUGH = "insp";
    private boolean isDebug = false;


    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class, true, false);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_PHONE_STATE");

    @Before
    public void setUp(){
        PreferencesManager.createInstance(getContext(), getBasicUser().getCredentials().login);
        if(PreferencesManager.getInstance().isDebug()){
            isDebug = true;
            PreferencesManager.getInstance().setDebug(false);
        }
        mActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown(){
        if (isDebug){
            PreferencesManager.getInstance().setDebug(true);
        }
    }


    @Test
    public void noPinTest() {
        BasicUser basicUser = Authorization.getInstance().getLastAuthUser();
        String pinCode = "";
        if(basicUser != null) {
            AuthorizationCache cache = new AuthorizationCache(getContext());
            pinCode = cache.readPin(basicUser.getCredentials().login);
        }
        if (!pinCode.isEmpty()) {
            return;
        }
        boolean noServer = false;
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            onView(withId(R.id.auth_no_server)).check(matches(isDisplayed()));
            noServer = true;
        } catch (AssertionFailedError e) {
            e.printStackTrace();
        }

        //Проверка TextView с версией и Toast с версией
        onView(withId(R.id.auth_version)).check(matches(withText(containsString("Версия:")))).perform(click());
        onView(withText(containsString("Версия приложения")))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //Поле логина. Очистка на случай если вставлен ник
        ViewInteraction etLogin = onView(withId(R.id.auth_login));
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
        etPassword.perform(scrollTo(), typeText(TOO_SHORT), closeSoftKeyboard());
        etPassword.check(matches(hasErrorText(AuthUtil.minLength(TOO_SHORT))));
        ibPasswordClear.check(matches(isDisplayed()));
        ibPasswordShow.check(matches(isDisplayed()));
        ibPasswordShow.perform(click());
        etPassword.check(matches(withInputType(InputType.TYPE_CLASS_TEXT)));
        ibPasswordClear.perform(click());
        etPassword.check(matches(withText("")));
        etPassword.perform(typeText(LONG_ENOUGH));

        //Кнопка "Войти"
        ViewInteraction btnSignIn = onView(withId(R.id.auth_sign_in));
        btnSignIn.perform(scrollTo(), click());
        if (noServer) {
            onView(anyOf(withText("Сервер не доступен"), withText(containsString("У приложения отсутствует доступ к серверу"))))
                    .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        } else {
            onView(withText("Логин или пароль введены не верно."))
                    .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            etLogin.perform(replaceText("inspector"));
            etPassword.perform(replaceText("inspector0"));
            btnSignIn.perform(click());

            //Успешная авторизация
            onView(withId(R.id.mainMenu_Toolbar)).perform(waitUntil(isDisplayed()));
            //Открываем NavigationDrawer
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
            //Открываем окно настроек
            onView(withText(getContext().getResources().getString(R.string.settings))).perform(click());
            //Включаем активацию по пин-коду
            onView(withText("Пин-код")).check(matches(isDisplayed())).perform(click());
            ViewInteraction btnOne = onView(withText("1"));
            ViewInteraction pinCodeLinLay = onView(withId(R.id.pinFragment_pclTop));
            pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
            btnOne.perform(click());
            pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_FILLED)));
            onView(withId(R.id.pin_clear)).perform(click());
            pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
            btnOne.perform(click()).perform(click()).perform(click()).perform(click());
            onView( withText(containsString("Подтвердите пин-код")))
                    .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            btnOne.perform(click()).perform(click()).perform(click());
            onView(withText("2")).perform(click());
            onView( withText("Пин-коды не совпадают, порпобуйте еще раз"))
                    .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            pinCodeLinLay.check(matches(withStatus(PinCodeLinLay.PinDotStatus.FIRST_CLEAR)));
            btnOne.perform(click()).perform(click()).perform(click()).perform(click());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            btnOne.perform(click()).perform(click()).perform(click()).perform(click());
            onView( withText(containsString("Вход по пин-коду активирован")))
                    .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            onView(withId(R.id.pin_forgot)).perform(click());
            onView(withText("Сбросить пин-код и авторизоаться через логин и пароль?")).check(matches(isDisplayed()));
            onView(withText("Да")).perform(click());

        }
    }

    @Test
    public void pinTest(){
        BasicUser basicUser = Authorization.getInstance().getLastAuthUser();
        String pinCode = "";
        if(basicUser != null) {
            AuthorizationCache cache = new AuthorizationCache(getContext());
            pinCode = cache.readPin(basicUser.getCredentials().login);
        }
        if (pinCode.isEmpty()) {
            return;
        }
        int x = Integer.parseInt(String.valueOf(pinCode.charAt(0)));
        if (x==9) {
            x--;
        }else {
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
        onView( withText("Неправильный пин"))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
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
        onView(withText(String.valueOf(pinCode.charAt(3)))).perform(click());
    }

    public static Matcher<View> withStatus(final PinCodeLinLay.PinDotStatus expectedStatus) {
        return new BoundedMatcher<View, PinCodeLinLay>(PinCodeLinLay.class) {

            @Override
            public void describeTo(org.hamcrest.Description description) {
            }

            @Override
            protected boolean matchesSafely(PinCodeLinLay item) {
                return item.getPinDotStatus()==expectedStatus;
            }

        };
    }
}
