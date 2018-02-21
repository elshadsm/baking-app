package com.elshadsm.baking.baking_app.network;

import com.elshadsm.baking.baking_app.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Elshad Seyidmammadov on 19.02.2018.
 */

public interface APIInterface {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
