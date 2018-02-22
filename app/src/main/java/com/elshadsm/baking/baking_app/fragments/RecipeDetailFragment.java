package com.elshadsm.baking.baking_app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elshadsm.baking.baking_app.R;
import com.elshadsm.baking.baking_app.activities.RecipeDetailActivity;
import com.elshadsm.baking.baking_app.adapters.RecipeDetailAdapter;
import com.elshadsm.baking.baking_app.models.Constants;
import com.elshadsm.baking.baking_app.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.recipe_details_recycler_view)
    RecyclerView recyclerView;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);
        applyConfiguration();
        return rootView;
    }

    private void applyConfiguration() {
        Recipe recipe = getArguments().getParcelable(Constants.RECIPE_DETAILS_FRAGMENT_ARGUMENT);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        RecipeDetailAdapter recipeDetailAdapter = new RecipeDetailAdapter(getContext(), (RecipeDetailActivity) getActivity());
        recyclerView.setAdapter(recipeDetailAdapter);
        recipeDetailAdapter.setRecipeData(recipe);
    }

}
