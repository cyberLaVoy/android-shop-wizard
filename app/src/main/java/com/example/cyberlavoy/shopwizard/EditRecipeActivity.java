package com.example.cyberlavoy.shopwizard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.Callable;

public class EditRecipeActivity extends AppCompatActivity {
    private static final String EXTRA_RECIPE_ID= "com.example.cyberlavoy.shopwizard.shopping_list_id";
    private String apiRecipesResourseUrl = "https://stormy-everglades-69504.herokuapp.com/recipes";
    private int mRecipeId;
    private Recipe mRecipe;

    public static Intent newIntent(Context packageContext, int recipeListId) {
        Intent intent = new Intent(packageContext, EditRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeListId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
    }
     private void updateUI() {
        final String[] responseArray = new String[1];
        RequestHandler.getInstance(getApplicationContext()).handleGETRequest(apiRecipesResourseUrl + "/" + Integer.toString(mRecipeId), responseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                String responseJson = responseArray[0];
                ResponseParser responseParser = new ResponseParser(getApplicationContext());
                ListStore.getInstance(getApplicationContext()).removeRecipe(mRecipeId);
                mRecipe = responseParser.parseRecipeJSONString(responseJson);
                return null;
            }
        });
    }

}
