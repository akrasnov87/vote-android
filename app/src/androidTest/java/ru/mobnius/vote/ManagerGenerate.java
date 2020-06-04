package ru.mobnius.vote;

import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.ui.activity.LoginToPinTest;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;

public abstract class ManagerGenerate extends DbGenerate {

    protected ManagerGenerate() {
        super();

        DataManager.createInstance(getDaoSession());
    }

    public static String getBaseUrl() {
        return MobniusApplication.getBaseUrl();
    }

    public static BasicCredentials getCredentials() {
        return new BasicCredentials("inspector", "inspector0");
    }

    protected static BasicUser getBasicUser() {
        return new BasicUser(getCredentials(), 4, ".inspector.");
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
    public class ViewVisibilityIdlingResource implements IdlingResource {

        private final View mView;
        private final int mExpectedVisibility;

        private boolean mIdle;
        private ResourceCallback mResourceCallback;

        public ViewVisibilityIdlingResource(final View view, final int expectedVisibility) {
            this.mView = view;
            this.mExpectedVisibility = expectedVisibility;
            this.mIdle = false;
            this.mResourceCallback = null;
        }

        @Override
        public final String getName() {
            return ViewVisibilityIdlingResource.class.getSimpleName();
        }

        @Override
        public final boolean isIdleNow() {
            mIdle = mIdle || mView.getVisibility() == mExpectedVisibility;

            if (mIdle) {
                if (mResourceCallback != null) {
                    mResourceCallback.onTransitionToIdle();
                }
            }

            return mIdle;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            mResourceCallback = resourceCallback;
        }

    }
}
