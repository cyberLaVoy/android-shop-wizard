package com.example.cyberlavoy.shopwizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class RecipesActivity extends AppCompatActivity {
    private static final String EXTRA_ADDING_RECIPES_TO_LIST = "com.example.cyberlavoy.shopwizard.adding_recipes_to_list";
    private String apiRecipesResourseUrl = "https://stormy-everglades-69504.herokuapp.com/recipes";
    private boolean mAddingRecipesToList;
    RecyclerView mRecipesRecyclerView;
    FloatingActionButton mAddRecipeFloatingActionButton;
    FloatingActionButton mSubmitRecipesFloatingActionButton;
    private RecipesAdapter mAdapter;
    private List<Integer> mSelectedRecipesIds = new ArrayList<>();

    public static Intent newIntent(Context packageContext, boolean addingRecipesToList) {
        Intent intent = new Intent(packageContext, RecipesActivity.class);
        intent.putExtra(EXTRA_ADDING_RECIPES_TO_LIST, addingRecipesToList);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        mAddingRecipesToList = getIntent().getBooleanExtra(EXTRA_ADDING_RECIPES_TO_LIST, false);
        mAddRecipeFloatingActionButton = findViewById(R.id.add_recipe_floating_action_btn);
        mSubmitRecipesFloatingActionButton = findViewById(R.id.submit_checked_recipes_floating_action_btn);
        mRecipesRecyclerView = findViewById(R.id.recipes_recycler_view);
        mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(RecipesActivity.this));
        mAddRecipeFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mSubmitRecipesFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", mSelectedRecipesIds.toString());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            }
        });
        if (!mAddingRecipesToList) {
            mSubmitRecipesFloatingActionButton.setVisibility(View.GONE);
        }
        else {
            mAddRecipeFloatingActionButton.setVisibility(View.GONE);
        }
        updateUI();
    }

    private void postNewRecipe() {
        final String[] responseArray = new String[1];
        RequestHandler.getInstance(getApplicationContext()).handlePOSTRequest(apiRecipesResourseUrl, null, responseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                JSONObject RecipeJsonObject = new JSONObject(responseArray[0]);
                int recipeId = Integer.parseInt(RecipeJsonObject.getString("recipe_id"));
                return null;
            }
        });
    }

    private void updateUI() {
        List<Recipe> Recipes = ListStore.getInstance(getApplicationContext()).getRecipes();
        if (mAdapter == null) {
            mAdapter = new RecipesAdapter(Recipes);
            mRecipesRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setRecipes(Recipes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Recipe mRecipe;
        TextView mRecipeLabelTextView;
        CheckBox mRecipeSelectionCheckBox;
        ImageButton mEditRecipeBtn;

        public RecipeHolder(@NonNull View RecipeView) {
            super(RecipeView);
            mRecipeLabelTextView = RecipeView.findViewById(R.id.recipe_list_recipe_label);
            mRecipeSelectionCheckBox = RecipeView.findViewById(R.id.recipe_selection_check_box);
            mEditRecipeBtn = RecipeView.findViewById(R.id.edit_recipe_btn);
            RecipeView.setOnClickListener(this);
        }

        public void bind(final Recipe recipe) {
            mRecipe = recipe;
            mRecipeLabelTextView.setText(recipe.getLabel());
            mEditRecipeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = EditRecipeActivity.newIntent(RecipesActivity.this, recipe.getListId());
                    startActivity(intent);
                }
            });
            if (mAddingRecipesToList) {
                mRecipeSelectionCheckBox.setChecked(mSelectedRecipesIds.contains(recipe.getListId()));
                mEditRecipeBtn.setVisibility(View.GONE);
            }
            else {
               mRecipeSelectionCheckBox.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            if (mAddingRecipesToList) {
                if (!mSelectedRecipesIds.contains(mRecipe.getListId())) {
                    mSelectedRecipesIds.add(mRecipe.getListId());
                    mRecipeSelectionCheckBox.setChecked(true);
                } else {
                    mSelectedRecipesIds.remove(Integer.valueOf(mRecipe.getListId()));
                    mRecipeSelectionCheckBox.setChecked(false);
                }
            }
        }
    }

    private class RecipesAdapter extends RecyclerView.Adapter<RecipeHolder> {
        List<Recipe> mRecipes;
        private RecipesAdapter(List<Recipe> recipes) {
            mRecipes = recipes;
        }
        @NonNull
        @Override
        public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View RecipeView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_recipe_item, parent, false);
            return new RecipeHolder(RecipeView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
            holder.bind(mRecipes.get(position));
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        public void setRecipes(List<Recipe> recipes) {
            mRecipes = recipes;
        }
    }
}
