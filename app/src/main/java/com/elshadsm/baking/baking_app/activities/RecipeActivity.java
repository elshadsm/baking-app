package com.elshadsm.baking.baking_app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elshadsm.baking.baking_app.R;
import com.elshadsm.baking.baking_app.adapters.RecipeAdapter;
import com.elshadsm.baking.baking_app.models.Recipe;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    static String ALL_RECIPES="ecipes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
    }

    @Override
    public void onListItemClick(Recipe clickedItem) {
//        Bundle selectedRecipeBundle = new Bundle();
//        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
//        selectedRecipe.add(selectedItem);
//        selectedRecipeBundle.putParcelableArrayList(SELECTED_RECIPES,selectedRecipe);

//        final Intent intent = new Intent(this, RecipeDetailActivity.class);
//        intent.putExtras(selectedRecipeBundle);
//        startActivity(intent);
    }
}
