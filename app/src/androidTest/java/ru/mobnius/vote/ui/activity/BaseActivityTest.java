package ru.mobnius.vote.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import junit.framework.AssertionFailedError;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.vote.VoteManager;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.data.storage.models.Question;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.component.PinCodeLinLay;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.ui.model.RouteItem;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public abstract class BaseActivityTest extends ManagerGenerate {
    private boolean noServer = false;
    private boolean isDebug = false;

    public final static String LOGIN = "1801-01";
    public final static String PASSWORD = "8849";

    @Rule
    public ActivityTestRule<LoginActivity> loginTestRule = new ActivityTestRule<>(LoginActivity.class, true, false);

    @Rule
    public ActivityTestRule<RouteListActivity> routeTestRule = new ActivityTestRule<>(RouteListActivity.class, true, false);
    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Before
    public void setUp() {
        PreferencesManager.createInstance(getContext(), getBasicUser().getCredentials().login);
        if (PreferencesManager.getInstance().isDebug()) {
            isDebug = true;
            PreferencesManager.getInstance().setDebug(false);
        }

        GlobalSettings.ENVIRONMENT = "test";

        BasicUser basicUser = Authorization.getInstance().getLastAuthUser();
        String pinCode = "";
        AuthorizationCache cache = new AuthorizationCache(getContext());
        if (basicUser != null) {
            pinCode = cache.readPin(basicUser.getCredentials().login);
        }
        if (!pinCode.isEmpty()) {
            return;
        }
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            //проверяем есть ли интернет и доступен ли сервер
            onView(withId(R.id.auth_no_server)).check(matches(isDisplayed()));
            noServer = true;
        } catch (AssertionFailedError e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        if (isDebug) {
            PreferencesManager.getInstance().setDebug(true);
        }
    }

    public static ViewAction waitUntil(final Matcher<View> matcher) {
        return actionWithAssertions(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                StringDescription description = new StringDescription();
                matcher.describeTo(description);
                return String.format("wait until: %s", description);
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (!matcher.matches(view)) {
                    LayoutChangeCallback callback = new LayoutChangeCallback(matcher);
                    try {
                        IdlingRegistry.getInstance().register(callback);
                        view.addOnLayoutChangeListener(callback);
                        uiController.loopMainThreadUntilIdle();
                    } finally {
                        view.removeOnLayoutChangeListener(callback);
                        IdlingRegistry.getInstance().unregister(callback);
                    }
                }
            }
        });
    }

    private static class LayoutChangeCallback implements IdlingResource, View.OnLayoutChangeListener {

        private Matcher<View> matcher;
        private IdlingResource.ResourceCallback callback;
        private boolean matched = false;

        LayoutChangeCallback(Matcher<View> matcher) {
            this.matcher = matcher;
        }

        @Override
        public String getName() {
            return "Layout change callback";
        }

        @Override
        public boolean isIdleNow() {
            return matched;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            matched = matcher.matches(v);
            callback.onTransitionToIdle();
        }
    }

    public static Matcher<View> hasIcon(final Drawable drawable) {
        return new BoundedMatcher<View, ActionMenuItemView>(ActionMenuItemView.class) {
            @Override
            public void describeTo(final Description description) {
            }

            @Override
            public boolean matchesSafely(final ActionMenuItemView actionMenuItemView) {
                boolean x = compareDrawable(actionMenuItemView.getItemData().getIcon(), drawable);
                return x;
            }
        };
    }

    public static boolean compareDrawable(Drawable d1, Drawable d2) {
        try {
            Bitmap bitmap1 = ((BitmapDrawable) d1).getBitmap();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
            stream1.flush();
            byte[] bitmapdata1 = stream1.toByteArray();
            stream1.close();

            Bitmap bitmap2 = ((BitmapDrawable) d2).getBitmap();
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            stream2.flush();
            byte[] bitmapdata2 = stream2.toByteArray();
            stream2.close();

            return Arrays.equals(bitmapdata1, bitmapdata2);
        } catch (Exception e) {
        }
        return false;
    }

    public int getRVLenght(ActivityTestRule activityTest, int resourceId) {
        activityTest.launchActivity(new Intent());
        RecyclerView recyclerView = (RecyclerView) activityTest.getActivity().findViewById(resourceId);
        return recyclerView.getAdapter().getItemCount();
    }

    public static Matcher<View> withStatus(final PinCodeLinLay.PinDotStatus expectedStatus) {
        return new BoundedMatcher<View, PinCodeLinLay>(PinCodeLinLay.class) {

            @Override
            public void describeTo(org.hamcrest.Description description) {
            }

            @Override
            protected boolean matchesSafely(PinCodeLinLay item) {
                return item.getPinDotStatus() == expectedStatus;
            }
        };
    }

    public boolean isServerUnavailable() {
        return noServer;
    }

    public String singleAnswer(){
        List<RouteItem> routes = DataManager.getInstance().getRouteItems(DataManager.RouteFilter.ALL);
        RouteItem routeItem = routes.get(0);
        List<PointItem> pointItems = DataManager.getInstance().getPointItems(routeItem.id, PointFilter.ALL);
        PointItem pointItem = pointItems.get(0);
        DataManager dataManager = DataManager.getInstance();
        List<Results> results = dataManager.getPointResults(pointItem.id);
        Question question = DataManager.getInstance().getQuestions()[0];
        // задание ранее выполнялось
        String singleAnswer ="";
        if (DataManager.getInstance().getPointState(pointItem.id).isDone()) {
            VoteManager voteManager = new VoteManager();
            voteManager.importFromResult(results.toArray(new Results[0]));
            long was = voteManager.getQuestionAnswer(question.id);
            Answer[] answers = dataManager.getAnswers(question.id);
            if(was > 0) {
                // на данный вопрос был данн ответ с индентификатором was
                for(Answer item : answers) {
                    if(item.id == was) {
                        singleAnswer = item.c_text;
                    }
                }
            }
        }
        return  singleAnswer;
    }
}
