package com.elshadsm.baking.baking_app.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.elshadsm.baking.baking_app.idling_resource.SimpleIdlingResource;
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

    @BindView(R.id.recipes_recycler_view)
    RecyclerView recyclerView;

    private Bundle savedInstanceState;
    private static final String SAVED_LAYOUT_MANAGER_KEY = "saved_layout_manager";
    SimpleIdlingResource idlingResource;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);
        this.savedInstanceState = savedInstanceState;
        appyConfiguration(rootView);
        return rootView;
    }

    private void appyConfiguration(View rootView) {
        idlingResource = (SimpleIdlingResource) ((RecipeActivity) getActivity()).getIdlingResource();
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
        RecipeAdapter recipesAdapter = new RecipeAdapter((RecipeActivity) getActivity());
        applyLayoutManager(rootView);
        recyclerView.setAdapter(recipesAdapter);
        fetchRecipeData(recipesAdapter);
    }

    private void applyLayoutManager(View rootView) {
        if (rootView.getTag() != null && rootView.getTag().equals("sw-600")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),
                    getResources().getInteger(R.integer.grid_view_landscape_column_number));
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
                restoreViewState();
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable throwable) {
                Log.e("http error: ", throwable.getMessage());
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_LAYOUT_MANAGER_KEY, recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    private void restoreViewState() {
        if (savedInstanceState == null) {
            return;
        }
        Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER_KEY);
        recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }

}
