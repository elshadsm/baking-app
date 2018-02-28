package com.elshadsm.baking.baking_app.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elshadsm.baking.baking_app.R;
import com.elshadsm.baking.baking_app.adapters.RecipeDetailAdapter;
import com.elshadsm.baking.baking_app.fragments.RecipeDetailFragment;
import com.elshadsm.baking.baking_app.fragments.StepDetailFragment;
import com.elshadsm.baking.baking_app.models.Constants;
import com.elshadsm.baking.baking_app.models.Recipe;
import com.elshadsm.baking.baking_app.models.Step;
import com.google.android.exoplayer2.C;

import java.util.ArrayList;

import static com.elshadsm.baking.baking_app.models.Constants.*;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener {

    private Recipe recipe;
    private FragmentManager fragmentManager;
    private RecipeDetailFragment recipeDetailFragment;
    private Bundle savedInstanceState;

    private static final String SAVED_STEP_SELECTED_INDEX_KEY = "saved_step_selected_index";
    private int stepSelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        this.savedInstanceState = savedInstanceState;
        fragmentManager = getSupportFragmentManager();
        loadDataFromExtras();
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

    private void updateActionBar(Recipe recipe) {
        assert recipe != null;
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(recipe.getName());
    }

    private void updatePageBody(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_DETAILS_FRAGMENT_ARGUMENT, recipe);
        recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_details_fragment_container, recipeDetailFragment)
                .commit();
        updateStepDetailFragment();
    }

    private void updateStepDetailFragment() {
        if (!isLargeScreen()) {
            return;
        }
        if (savedInstanceState != null) {
            stepSelectedIndex = savedInstanceState.getInt(SAVED_STEP_SELECTED_INDEX_KEY, 0);
        }
        openStepDetailFragment(stepSelectedIndex);
    }

    @Override
    public void onListItemClick(int index) {
        if (isLargeScreen()) {
            this.stepSelectedIndex = index;
            openStepDetailFragment(index);
            return;
        }
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putParcelableArrayListExtra(INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST, new ArrayList<>(recipe.getSteps()));
        intent.putExtra(INTENT_EXTRA_NAME_STEP_DETAILS_INDEX, index);
        startActivity(intent);
    }

    private void openStepDetailFragment(int index) {
        Step step = recipe.getSteps().get(index);
        recipeDetailFragment.setSelectionIndex(index);
        Bundle args = new Bundle();
        args.putParcelable(STEP_DETAILS_FRAGMENT_ARGUMENT, step);
        args.putBoolean(STEP_DETAILS_FRAGMENT_FULLSCREEN_ARGUMENT, false);
        args.putLong(STEP_DETAILS_FRAGMENT_VIDEO_POSITION_ARGUMENT, C.TIME_UNSET);
        final StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.step_details_fragment_container, stepDetailFragment)
                .commit();
    }

    private boolean isLargeScreen() {
        return findViewById(R.id.activity_recipe_detail).getTag() != null &&
                findViewById(R.id.activity_recipe_detail).getTag().equals("sw600");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SAVED_STEP_SELECTED_INDEX_KEY, stepSelectedIndex);
        super.onSaveInstanceState(outState);
    }
}
