package ru.mobnius.vote.ui.activity;

import android.view.Gravity;
import android.widget.AutoCompleteTextView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.vote.VoteManager;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.data.storage.models.Question;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.ui.model.RouteItem;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class CPointListActivityTest extends BaseActivityTest {
    private boolean isDebug;


    @Before
    public void setUp() {
        PreferencesManager.createInstance(getContext(), getBasicUser().getCredentials().login);
        if (PreferencesManager.getInstance().isDebug()) {
            isDebug = true;
        }
    }

    @After
    public void tearDown() {
        if (Authorization.getInstance().getUser()!= null) {
            String login = Authorization.getInstance().getUser().getCredentials().login;
            MobniusApplication application = (MobniusApplication) loginTestRule.getActivity().getApplication();
            application.unAuthorized(true);

            getContext().deleteDatabase(login + ".db");
        }

    }

    @Test
    public void agitatorTest() {
        boolean noServer = false;
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
        onView(withId(R.id.mainMenu_Toolbar)).perform(waitUntil(isDisplayed()));
        onView(withId(R.id.mainMenuDrawerLayout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(allOf(withText(getContext().getResources().getString(R.string.synchronization)), not(withId(R.id.house_sync)))).perform(click());
        onView(withId(R.id.sync_start)).perform(click());
        onView(withText("OK")).inRoot(isDialog()) // <---
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        try {
            onView(withId(R.id.statistic_close)).perform(waitUntil(isDisplayed()), click());
        } catch (NoMatchingViewException e) {
            e.printStackTrace();
        }
        if (getRVLenght(routeTestRule, R.id.house_list) > 0) {

            onView(withId(R.id.mainMenu_Toolbar)).perform(waitUntil(isDisplayed()));
            onView(withId(R.id.house_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, scrollTo()));
            onView(withId(R.id.house_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.point_search)).check(matches(isDisplayed()));
            onView(withId(R.id.point_search)).perform(click());
            onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText("1"));
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
            onView(withId(R.id.house_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            Question question = DataManager.getInstance().getQuestions()[0];

            // в случае если ранее не выполнялся опрос
            if (singleAnswer().isEmpty()) {
                onView(withId(R.id.point_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

                Answer[] answers = DataManager.getInstance().getAnswers(question.id);
                for (Answer answer : answers) {
                    onView(withText(answer.c_text)).check(matches(isDisplayed()));
                }
                onView(withText(answers[4].c_text)).perform(click());
                onView(withId(R.id.point_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
                onView(withId(R.id.choice_document_info)).perform(click());
                onView(withId(R.id.point_info_reset)).perform(click());
                onView(withText("Да")).inRoot(isDialog()) // <---
                        .check(matches(isDisplayed()))
                        .perform(click());
                onView(withId(R.id.question_item_answers)).perform(RecyclerViewActions.actionOnItemAtPosition(answers.length - 1, scrollTo()));
                onView(withText("никого нет дома")).perform(click());
                onView(withId(R.id.point_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
                onView(withId(R.id.choice_document_info)).perform(click());
                onView(withId(R.id.point_info_reset)).perform(click());
                onView(withText("Да")).inRoot(isDialog()) // <---
                        .check(matches(isDisplayed()))
                        .perform(click());
                // в случае если ответ только один
            }else {
                onView(withId(R.id.point_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
                onView(withText(singleAnswer())).check(matches(isDisplayed()));
                onView(withId(R.id.choice_document_info)).perform(click());
                onView(withId(R.id.point_info_reset)).check(matches(isDisplayed()));
            }
       }
    }
}