package com.elshadsm.baking.baking_app.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elshadsm.baking.baking_app.R;
import com.elshadsm.baking.baking_app.activities.RecipeActivity;
import com.elshadsm.baking.baking_app.adapters.RecipeAdapter;
import com.elshadsm.baking.baking_app.models.Recipe;
import com.elshadsm.baking.baking_app.network.APIClient;
import com.elshadsm.baking.baking_app.network.APIInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment {

    @BindView(R.id.recipes_recycler_view) RecyclerView recyclerView;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);
        RecipeAdapter recipesAdapter = new RecipeAdapter((RecipeActivity) getActivity());
        recyclerView.setAdapter(recipesAdapter);
        applyLayoutManager(rootView);
        fetchRecipeData(recipesAdapter);
        return rootView;
    }

    private void applyLayoutManager(View rootView) {
        if (rootView.getTag() != null && rootView.getTag().equals("phone-land")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), R.integer.grid_view_landscape_column_number);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }
    }

    private void fetchRecipeData(final RecipeAdapter recipesAdapter) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ArrayList<Recipe>> call = apiInterface.getRecipe();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> recipeList = response.body();
                recipesAdapter.setRecipeData(recipeList, getContext());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable throwable) {
                Log.e("http error: ", throwable.getMessage());
            }
        });
    }

}