package com.example.bake_it.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bake_it.R;


public class StepDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID_STEP = "recipe_step";
    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_detail_activity);

        //restore fragment
        if (savedInstanceState == null) {

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();

            Integer recipe = extras.getInt(EXTRA_RECIPE_ID);
            Integer step = extras.getInt(EXTRA_RECIPE_ID_STEP);

            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(recipe, step);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_view_fragment, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
    }
}
