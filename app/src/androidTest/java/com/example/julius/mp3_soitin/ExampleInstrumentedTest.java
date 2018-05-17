package com.example.julius.mp3_soitin;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.julius.mp3_soitin", appContext.getPackageName());
    }

    @Test
    public void tabs() throws InterruptedException {
        Matcher<View> matcher = allOf(withText("Albums"),
                isDescendantOfA(withId(R.id.tabs)));
        onView(matcher).perform(click());

        onView(withText("Albums")).check(matches(isSelected()));

        onView(withText("PlayLists")).perform(click());
        onView(withText("PlayLists")).check(matches(isSelected()));
        onView(withId(R.id.playListRoot)).perform(swipeLeft());
        onView(withId(R.id.title)).check(matches(withText("NO TRACK SELECTED")));
        onView(withId(R.id.playerRootView)).perform(withCustomConstraints(swipeRight(), isDisplayed()));
        onView(withId(R.id.playListRoot)).perform(withCustomConstraints(swipeRight(), isDisplayed()));
        onView(withId(R.id.albumRootView)).perform(withCustomConstraints(swipeRight(), isDisplayed()));
        Thread.sleep(1000);
        onData(anything())
                .inAdapterView(allOf(withId(android.R.id.list), isCompletelyDisplayed()))
                .atPosition(0).perform(click());
        onView(withId(R.id.title)).check(matches(withText("Battle of Lepanto")));
    }

    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
    }

}
