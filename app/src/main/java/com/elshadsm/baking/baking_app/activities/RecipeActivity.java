package com.elshadsm.baking.baking_app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elshadsm.baking.baking_app.R;
import com.elshadsm.baking.baking_app.adapters.RecipeAdapter;
import com.elshadsm.baking.baking_app.models.Recipe;

import java.util.ArrayList;

import static com.elshadsm.baking.baking_app.models.Constants.INTENT_EXTRA_NAME_RECIPE_DETAILS;

public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
    }

    @Override
    public void onListItemClick(Recipe selectedRecipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_NAME_RECIPE_DETAILS, selectedRecipe);
        startActivity(intent);
    }
}
