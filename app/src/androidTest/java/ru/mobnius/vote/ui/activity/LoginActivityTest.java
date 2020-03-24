package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.utils.VersionUtil;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@SdkSuppress(maxSdkVersion = Build.VERSION_CODES.O_MR1)
@Ignore
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private String versionName;
    private Context appContext;
    @Rule

    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        versionName = VersionUtil.getShortVersionName(appContext);
    }

    @Test
    public void existsFields() {
        keyBoardHidden();
        onView(withText(appContext.getString(R.string.enter))).check(matches(anything())).perform(click());
    }

    @Test
    public void existsToast() {
        keyBoardHidden();
        onView(withText(containsString(versionName))).perform(click());
        onView(withText(containsString(versionName))).inRoot(withDecorView(is(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    private void keyBoardHidden() {
        InputMethodManager im = (InputMethodManager) appContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = mActivityRule.getActivity().getCurrentFocus();
        Objects.requireNonNull(im).hideSoftInputFromWindow(Objects.requireNonNull(view).getWindowToken(), 0);
    }
}