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

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.INTENT_EXTRA_NAME_RECIPE_DETAILS)) {
            Bundle data = intent.getExtras();
            assert data != null;
            Recipe recipe = data.getParcelable(Constants.INTENT_EXTRA_NAME_RECIPE_DETAILS);
            updateActionBar(recipe);
            updatePageBody(recipe);
        }
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
        final RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_details_fragment_container, fragment)
                .addToBackStack(Constants.RECIPE_DETAILS_FRAGMENT_STACK)
                .commit();
    }

    @Override
    public void onListItemClick(int index) {

    }
}
