package com.example.bake_it;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.bake_it.ui.MainListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class allActivitiesBasicTest {

    @Rule
    public ActivityTestRule<MainListActivity> mActivityTestRule = new ActivityTestRule<>(MainListActivity.class);

    @Test
    public void mainListActivityTest() {

        // Check correct recipe name displayed at rv_image_placeholder specified position
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPosition(1))
                .check(matches(hasDescendant(withText("Brownies"))));

        // Click on the item in rv_image_placeholder MainListActivity
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Check StepsListActivity title is correct
        onView(withText("Brownies")).check(matches(isDisplayed()));

        // Verify ingredients card
        onView(withId(R.id.ingredients_card)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_header)).check(matches(withText("Ingredients")));
        onView(new RecyclerViewMatcher(R.id.ingredients_rv).atPosition(1))
                .check(matches(hasDescendant(withText("unsalted butter"))));

        // Verify correct step is displayed in the steps list
        onView(new RecyclerViewMatcher(R.id.steps_rv).atPosition(2))
                .check(matches(hasDescendant(withText("2. Melt butter and bittersweet chocolate."))));

        // Click on rv_image_placeholder step and verify exoplayer is displayed
        onView(withId(R.id.steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.player_view_LL)).check(matches(isDisplayed()));

        //go back and check exoplayer not displayed
        Espresso.pressBack();
        onView(withId(R.id.steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(withId(R.id.player_view_LL)).check(matches(not(isDisplayed())));
    }
}
