package com.fraz.dartlog;

import android.content.Context;
import android.content.Intent;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.fraz.dartlog.game.x01.X01GameActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class X01GameTest {

    @Rule
    public ActivityTestRule<X01GameActivity> mActivityRule =
            new ActivityTestRule<X01GameActivity>(X01GameActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, MainActivity.class);
                    ArrayList<String> playerNames = new ArrayList<>();
                    playerNames.add("Filip");
                    playerNames.add("Razmus");
                    intent.putStringArrayListExtra("playerNames", playerNames);
                    intent.putExtra("x", 3);
                    return intent;
                }
            };

    @Test
    public void testNumPadInput() {
        onView(withId(R.id.submit_button)).check(matches(withText("No score")));

        // Press buttons on the numpad.
        onView(withId(R.id.one)).perform(click());
        onView(withId(R.id.two)).perform(click());
        onView(withId(R.id.three)).perform(click());

        // Check that the numpad score field and submit button is updated.
        onView(withId(R.id.score_view)).check(matches(withText("123")));
        onView(withId(R.id.submit_button)).check(matches(withText("SUBMIT")));

        // Check that erase removes the last digit.
        onView(withId(R.id.erase)).perform(click());
        onView(withId(R.id.score_view)).check(matches(withText("12")));
        onView(withId(R.id.submit_button)).check(matches(withText("SUBMIT")));

        // Check that when erasing all digits the score field displays 0 and submit button
        // displays "no score".
        onView(withId(R.id.erase)).perform(click());
        onView(withId(R.id.erase)).perform(click());
        onView(withId(R.id.score_view)).check(matches(withText("0")));
        onView(withId(R.id.submit_button)).check(matches(withText("No score")));

        // Check that score field shows 0 and that submit button is set to "no score" after
        // submit button is pressed.
        onView(withId(R.id.one)).perform(click());
        onView(withId(R.id.two)).perform(click());
        onView(withId(R.id.submit_button)).perform(click());
        onView(withId(R.id.score_view)).check(matches(withText("0")));
        onView(withId(R.id.submit_button)).check(matches(withText("No score")));

        //Check that scores above 180 is not possible to input
        onView(withId(R.id.one)).perform(click());
        onView(withId(R.id.eight)).perform(click());
        onView(withId(R.id.one)).perform(click());
        onView(withId(R.id.score_view)).check(matches(withText("18")));
        onView(withId(R.id.zero)).perform(click());
        onView(withId(R.id.score_view)).check(matches(withText("180")));
        onView(withId(R.id.zero)).perform(click());
        onView(withId(R.id.score_view)).check(matches(withText("180")));
    }
}