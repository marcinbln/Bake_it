package com.example.bake_it.data.network;


import com.example.bake_it.data.db.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitClient {

    // url
    // https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json

    @GET("baking.json")
    Call<Recipe[]> getRecipes();

}


