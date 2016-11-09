package com.fraz.dartlog;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.fraz.dartlog.game.x01.X01GameActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        // Press buttons on the numpad.
        onView(withId(R.id.one)).perform(click());
        onView(withId(R.id.two)).perform(click());
        onView(withId(R.id.three)).perform(click());

        // Check that the numpad score field is updated.
        onView(withId(R.id.score_input)).check(matches(withText("123")));

        // Check that erase removes the last digit.
        onView(withId(R.id.erase)).perform(click());
        onView(withId(R.id.score_input)).check(matches(withText("12")));

        // Check that when erasing all digits the score field displays 0.
        onView(withId(R.id.erase)).perform(click());
        onView(withId(R.id.erase)).perform(click());
        onView(withId(R.id.score_input)).check(matches(withText("0")));

        // Check that 0 is displayed after enter is pressed.
        onView(withId(R.id.one)).perform(click());
        onView(withId(R.id.two)).perform(click());
        onView(withId(R.id.enter)).perform(click());
        onView(withId(R.id.score_input)).check(matches(withText("0")));

    }
}