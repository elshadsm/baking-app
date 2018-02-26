package com.elshadsm.baking.baking_app.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elshadsm.baking.baking_app.R;
import com.elshadsm.baking.baking_app.adapters.RecipeDetailAdapter;
import com.elshadsm.baking.baking_app.fragments.RecipeDetailFragment;
import com.elshadsm.baking.baking_app.models.Constants;
import com.elshadsm.baking.baking_app.models.Recipe;

import java.util.ArrayList;

import static com.elshadsm.baking.baking_app.models.Constants.INTENT_EXTRA_NAME_STEP_DETAILS_INDEX;
import static com.elshadsm.baking.baking_app.models.Constants.INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener {

    private Recipe recipe;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        fragmentManager = getSupportFragmentManager();
        loadDataFromExtras();
        if (savedInstanceState != null) {
            loadDataFromSavedInstance(savedInstanceState);
        }
    }

    private void loadDataFromExtras() {
        Intent intent = getIntent();
        if (!intent.hasExtra(Constants.INTENT_EXTRA_NAME_RECIPE_DETAILS)) {
            return;
        }
        Bundle data = intent.getExtras();
        assert data != null;
        recipe = data.getParcelable(Constants.INTENT_EXTRA_NAME_RECIPE_DETAILS);
        updatePageBody(recipe);
        updateActionBar(recipe);
    }

    private void loadDataFromSavedInstance(Bundle savedInstanceState) {

    }

    private void updateActionBar(Recipe recipe) {
        assert recipe != null;
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(recipe.getName());
    }

    private void updatePageBody(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_DETAILS_FRAGMENT_ARGUMENT, recipe);
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_details_fragment_container, recipeDetailFragment)
//                .addToBackStack(Constants.RECIPE_DETAILS_FRAGMENT_STACK)
                .commit();
    }

    @Override
    public void onListItemClick(int index) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putParcelableArrayListExtra(INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST, new ArrayList<>(recipe.getSteps()));
        intent.putExtra(INTENT_EXTRA_NAME_STEP_DETAILS_INDEX, index);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
