package com.example.bake_it.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.bake_it.R;

import static com.example.bake_it.Constants.EXTRA_RECIPE_ID;


public class StepsListActivity extends AppCompatActivity implements StepsListFragment.OnFragmentInteractionListener {

    private Integer mOpenRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps_list_activity);

        Intent intent = getIntent();
        mOpenRecipe = intent.getIntExtra(EXTRA_RECIPE_ID, 99);

        if (savedInstanceState == null) {
            //portrait view - step list view
            StepsListFragment stepsListFragment = StepsListFragment.newInstance(mOpenRecipe);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_view_fragment, stepsListFragment)
                    .commit();

            if (MainListActivity.tabletFlag) {
                // enable landscape mode - default set to portrait in manifest
                // tablet view - load detail fragment
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                Integer mOpenStep = 0;
                StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mOpenRecipe, mOpenStep);
                fragmentManager.beginTransaction()
                        .add(R.id.step_fragment_landscape, stepDetailFragment)
                        .commit();
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

        }

    }

    @Override
    public void onFragmentInteraction(Integer integer) {

        if (MainListActivity.tabletFlag) {
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mOpenRecipe, integer);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_fragment_landscape, stepDetailFragment)
                    .commit();
        } else {

            Intent intent = new Intent(this, StepDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putInt(StepDetailActivity.EXTRA_RECIPE_ID, mOpenRecipe);
            extras.putInt(StepDetailActivity.EXTRA_RECIPE_ID_STEP, integer);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }


}
