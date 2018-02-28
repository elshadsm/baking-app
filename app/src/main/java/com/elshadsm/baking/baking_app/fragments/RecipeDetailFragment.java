package com.elshadsm.baking.baking_app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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

    RecipeDetailAdapter recipeDetailAdapter;
    private Bundle savedInstanceState;
    private static final String SAVED_LAYOUT_MANAGER_KEY = "saved_layout_manager_detail";
    private int stepSelectedIndex = -1;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);
        this.savedInstanceState = savedInstanceState;
        applyConfiguration();
        return rootView;
    }

    private void applyConfiguration() {
        Recipe recipe = getArguments().getParcelable(Constants.RECIPE_DETAILS_FRAGMENT_ARGUMENT);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recipeDetailAdapter = new RecipeDetailAdapter(getContext(), (RecipeDetailActivity) getActivity());
        recyclerView.setAdapter(recipeDetailAdapter);
        recipeDetailAdapter.setRecipeData(recipe);
        restoreViewState();
        restoreSelectionIndex();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_LAYOUT_MANAGER_KEY, recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    public void restoreViewState() {
        if (savedInstanceState == null) {
            return;
        }
        Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER_KEY);
        recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }

    public void restoreSelectionIndex() {
        if (stepSelectedIndex != -1) {
            recipeDetailAdapter.setSelectionIndex(stepSelectedIndex);
        }
    }

    public void setSelectionIndex(int index) {
        stepSelectedIndex = index;
        if (recipeDetailAdapter != null) {
            recipeDetailAdapter.setSelectionIndex(index);
        }
    }

}
