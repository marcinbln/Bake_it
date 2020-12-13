package com.example.bake_it;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.bake_it.ui.StepsListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.bake_it.Constants.EXTRA_RECIPE_ID;

@RunWith(AndroidJUnit4ClassRunner.class)
public class StepsListActivityTest {

    @Rule
    public IntentsTestRule<StepsListActivity> mActivityTestRule = new IntentsTestRule<>(StepsListActivity.class);


    @Test
    public void mainListActivityTest() {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_RECIPE_ID, 2);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        intending(toPackage("com.example.bake_it")).respondWith(result);

        onView(withText("Brownies")).check(matches(isDisplayed()));


    }


}
