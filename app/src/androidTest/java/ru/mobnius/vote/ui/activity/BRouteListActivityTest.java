package ru.mobnius.vote.ui.activity;

import android.content.Intent;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.contrib.RecyclerViewActions;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class BRouteListActivityTest extends BaseActivityTest {
    private boolean isDebug;


    @Before
    public void setUp() {
        PreferencesManager.createInstance(getContext(), getBasicUser().getCredentials().login);
        if (PreferencesManager.getInstance().isDebug()) {
            isDebug = true;
        }
        loginTestRule.launchActivity(new Intent());
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
            if (!isDebug && noServer) {
                return;
            }
            if (!isDebug) {
                onView(withId(R.id.auth_login)).perform(replaceText(LOGIN), closeSoftKeyboard());
                onView(withId(R.id.auth_password)).perform(replaceText(PASSWORD), closeSoftKeyboard());
                onView(withId(R.id.auth_sign_in)).perform(scrollTo(), click());
            }
        }
        try {
            onView(withId(R.id.statistic_close)).perform(click());
        } catch (NoMatchingViewException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.mainMenu_Toolbar)).perform(waitUntil(isDisplayed()));
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(allOf(withText(getContext().getResources().getString(R.string.synchronization)), not(withId(R.id.house_sync)))).perform(click());
        onView(withId(R.id.sync_start)).perform(click());
        onView(withText("OK")).inRoot(isDialog()) // <---
                .check(matches(isDisplayed()))
                .perform(click());
        try {
            onView(withId(R.id.statistic_close)).perform(waitUntil(isDisplayed()), click());
        } catch (NoMatchingViewException e) {
            e.printStackTrace();
        }
        if (getRVLenght(routeTestRule, R.id.house_list) > 0) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                onView(withId(R.id.statistic_close)).perform(click());
            } catch (NoMatchingViewException e) {
                e.printStackTrace();
            }
            onView(withId(R.id.house_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.point_filter)).check(matches(isDisplayed()));
            onView(withId(R.id.point_search)).check(matches(isDisplayed()));
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
            onView(withId(R.id.action_route_filters)).check(matches(isDisplayed()));
        }
    }

}