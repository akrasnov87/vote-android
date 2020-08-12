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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Rule;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.GlobalSettings;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;

public abstract class BaseActivityTest extends ManagerGenerate {

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
        GlobalSettings.ENVIRONMENT = "test";
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
                boolean x= compareDrawable(actionMenuItemView.getItemData().getIcon(), drawable);
                return x;
            }
        };
    }
    public static boolean compareDrawable(Drawable d1, Drawable d2){
        try{
            Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
            stream1.flush();
            byte[] bitmapdata1 = stream1.toByteArray();
            stream1.close();

            Bitmap bitmap2 = ((BitmapDrawable)d2).getBitmap();
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            stream2.flush();
            byte[] bitmapdata2 = stream2.toByteArray();
            stream2.close();

            return Arrays.equals(bitmapdata1, bitmapdata2);
        }
        catch (Exception e) {
        }
        return false;
    }
    public int getRVLenght(ActivityTestRule activityTest, int resourceId) {
        activityTest.launchActivity(new Intent());
        RecyclerView recyclerView = (RecyclerView) activityTest.getActivity().findViewById(resourceId);
        return recyclerView.getAdapter().getItemCount();
    }
}
