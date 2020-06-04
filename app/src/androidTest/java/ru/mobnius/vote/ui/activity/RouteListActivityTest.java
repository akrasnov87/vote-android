package ru.mobnius.vote.ui.activity;

import android.content.Intent;
import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.ui.component.PinCodeLinLay;

import static androidx.test.espresso.Espresso.onView;
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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import static org.junit.Assert.*;

public class RouteListActivityTest extends ManagerGenerate {
    private boolean isDebug;

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
    public void setUp() {
        PreferencesManager.createInstance(getContext(), getBasicUser().getCredentials().login);
        if (PreferencesManager.getInstance().isDebug()) {
            isDebug = true;
        }
        mActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void routeListTest() {
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
        BasicUser basicUser = Authorization.getInstance().getLastAuthUser();
        String pinCode = "";
        if (basicUser != null) {
            AuthorizationCache cache = new AuthorizationCache(getContext());
            pinCode = cache.readPin(basicUser.getCredentials().login);
        }
        if (!pinCode.isEmpty()) {
            onView(withText(String.valueOf(pinCode.charAt(0)))).perform(click());
            onView(withText(String.valueOf(pinCode.charAt(1)))).perform(click());
            onView(withText(String.valueOf(pinCode.charAt(2)))).perform(click());
            onView(withText(String.valueOf(pinCode.charAt(3)))).perform(click());
        } else {
            if (!isDebug&&noServer){
                return;
            }
            if (!isDebug) {
                onView(withId(R.id.auth_login)).perform(replaceText("inspector"), closeSoftKeyboard());
                onView(withId(R.id.auth_password)).perform(replaceText("inspector0"), closeSoftKeyboard());
                onView(withId(R.id.auth_sign_in)).perform(scrollTo(), click());
            }
        }
        onView(withId(R.id.mainMenu_Toolbar)).perform(waitUntil(isDisplayed()));
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(allOf(withText(getContext().getResources().getString(R.string.synchronization)), not(withId(R.id.house_sync)))).perform(click());
        onView(withId(R.id.sync_start)).perform(click());
        onView(withText("Отмена")).check().perform(click());
        onView(withText("ok")).perform(click());
        onView(withId(R.id.sync_start)).perform(click());
        onView(withText("ok")).perform(waitUntil(isDisplayed()));
        onView(withText("ok")).perform(click());



    }

}