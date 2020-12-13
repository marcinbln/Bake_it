package com.example.bake_it.data.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bake_it.AppExecutors;
import com.example.bake_it.data.network.RetrofitClient;
import com.example.bake_it.data.network.RetrofitServiceBuilder;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesRepository {

    private static RecipesRepository instance;
    private final MutableLiveData<Recipe[]> mRecipes;
    private Recipe[] mRecipesArray;

    private RecipesRepository(Context context) {

        mRecipes = new MediatorLiveData<>();

        downloadData(context);

    }

    public static RecipesRepository getInstance(Context context) {

        if (instance == null) {
            instance = new RecipesRepository(context);
        }
        return instance;
    }

    public MutableLiveData<Recipe[]> getRecipesRepo() {
        return mRecipes;
    }

    public void downloadData(Context context) {

        RetrofitClient service = RetrofitServiceBuilder.buildService(RetrofitClient.class);
        Call<Recipe[]> call = service.getRecipes();

        call.enqueue(new Callback<Recipe[]>() {
            @Override
            public void onResponse(@NonNull Call<Recipe[]> call, @NonNull Response<Recipe[]> response) {

                Recipe[] listView = response.body();

                AppExecutors.getInstance().diskIO().execute(() -> {

                    if (listView != null) {
                        AppDatabase.getInstance(context).recipeDao().insertRecipes(Arrays.asList(listView));
                    }
                });

                mRecipes.postValue(listView);
                mRecipesArray = listView;

            }

            @Override
            public void onFailure(@NonNull Call<Recipe[]> call, @NonNull Throwable t) {
                Log.v("onFailure", t.toString());
            }
        });

    }

    public Recipe[] getRecipesArray() {
        return mRecipesArray;
    }
}
