package com.example.bake_it.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.test.espresso.IdlingResource;

import com.example.bake_it.R;
import com.example.bake_it.SimpleIdlingResource;
import com.example.bake_it.adapters.MainListAdapter;
import com.example.bake_it.data.db.Recipe;
import com.example.bake_it.data.db.RecipesRepository;
import com.example.bake_it.viewModels.MainListViewModel;
import com.example.bake_it.widget.BakingWidgetProvider;

import java.util.Arrays;

import static com.example.bake_it.Constants.EXTRA_RECIPE_ID;
import static com.example.bake_it.Constants.EXTRA_RECIPE_NAME;
import static com.example.bake_it.Constants.SPAN_COUNT_ONE;

public class MainListActivity extends AppCompatActivity implements MainListAdapter.ItemClickListener {

    public static boolean tabletFlag = false;
    private MainListAdapter adapter;
    private TextView emptyRv;
    private Recipe[] mRecipes;
    private SwipeRefreshLayout swipeRefreshLayout;
    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns mIdlingResource
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);
        tabletFlag = getResources().getBoolean(R.bool.isTablet);

        emptyRv = findViewById(R.id.tv_no_data);

        swipeRefreshLayout = findViewById(R.id.main_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        // List hidden by default, will be shown if data is downloaded
        RecyclerView rvRecipes = findViewById(R.id.recycler_view);
        rvRecipes.setVisibility(View.INVISIBLE);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, SPAN_COUNT_ONE);

        if (tabletFlag) {
            //if tablet - set to gridlayout to three columns
            mLayoutManager = new GridLayoutManager(this, 2);
            rvRecipes.setLayoutManager(mLayoutManager);
        }

        rvRecipes.setLayoutManager(mLayoutManager);

        adapter = new MainListAdapter(this);

        MainListViewModel mainListViewModel = new ViewModelProvider(this).get(MainListViewModel.class);

        //submit the list once downloaded
        mainListViewModel.getRecipesVM().observe(this, new Observer<Recipe[]>() {
            @Override
            public void onChanged(Recipe[] recipes) {

                mRecipes = recipes;
                adapter.submitList(Arrays.asList(recipes));
                MainListActivity.this.setIdle();

                // when no data show empty text view
                if (recipes.length == 0) {
                    emptyRv.setVisibility(View.VISIBLE);
                }
                // when there is data hide empty textview and show recycler view
                else {
                    emptyRv.setVisibility(View.GONE);
                    rvRecipes.setVisibility(View.VISIBLE);
                }
            }
        });
        rvRecipes.setAdapter(adapter);
    }


    @Override
    public void onItemClickListener(int itemID) {

        int[] ids = AppWidgetManager.getInstance(getApplicationContext()).getAppWidgetIds(new ComponentName(getApplication(), BakingWidgetProvider.class));

        String recipeTitle = mRecipes[itemID].getName();

        // Send broadcast to BakingWidgetProvider with the open recipe number and title
        Intent recipeIntent = new Intent(this, BakingWidgetProvider.class);
        recipeIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        recipeIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        recipeIntent.putExtra(EXTRA_RECIPE_ID, itemID);
        recipeIntent.putExtra(EXTRA_RECIPE_NAME, recipeTitle);
        sendBroadcast(recipeIntent);

        // Intent to open StepsListActivity.class
        Intent intent = new Intent(MainListActivity.this, StepsListActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, itemID);
        startActivity(intent);

    }

    private void refreshData() {

        swipeRefreshLayout.setRefreshing(true);
        RecipesRepository.getInstance(getApplicationContext()).downloadData(getApplicationContext());
        swipeRefreshLayout.setRefreshing(false);

    }

    @VisibleForTesting
    private void setIdle() {

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }

    }
}
