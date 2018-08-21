package com.example.cyberlavoy.shopwizard;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.concurrent.Callable;

public class HomeActivity extends AppCompatActivity {

    private Button mShoppingListsButton;
    private Button mRecipesButton;
    private Button mItemsButton;
    private FloatingActionButton mMenuDisplayFloatingActionBtn;
    private ConstraintLayout mOptionsMenu;
    private TextView mNewShoppingListMenuOption;
    private TextView mNewRecipeMenuOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mShoppingListsButton = findViewById(R.id.home_shopping_lists_btn);
        mRecipesButton = findViewById(R.id.home_recipes_btn);
        mItemsButton = findViewById(R.id.home_items_btn);
        mMenuDisplayFloatingActionBtn = findViewById(R.id.home_floating_action_btn);
        mOptionsMenu = findViewById(R.id.home_options_menu);
        mNewShoppingListMenuOption = findViewById(R.id.home_new_shopping_list_option);
        mNewRecipeMenuOption = findViewById(R.id.home_new_recipe_option);

        mShoppingListsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ShoppingListsActivity.newIntent(HomeActivity.this);
                startActivity(intent);
            }
        });
        mRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RecipesActivity.newIntent(HomeActivity.this, false);
                startActivity(intent);
            }
        });
        mItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ItemsActivity.newIntent(HomeActivity.this, false);
                startActivity(intent);
            }
        });
        mMenuDisplayFloatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleOptionsMenu();
            }
        });
        mNewShoppingListMenuOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNewShoppingList();
            }
        });
        mNewRecipeMenuOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNewRecipe();
            }
        });
    }
     private void toggleOptionsMenu() {
        if (mOptionsMenu.getVisibility() == View.GONE) {
            mOptionsMenu.setVisibility(View.VISIBLE);
            mMenuDisplayFloatingActionBtn.setImageResource(R.drawable.ic_clear_black);
        }
        else {
            mOptionsMenu.setVisibility(View.GONE);
            mMenuDisplayFloatingActionBtn.setImageResource(R.drawable.ic_add_black);
        }
    }
     private void postNewRecipe() {
        final String[] responseArray = new String[1];
        String apiRecipesResourseUrl = "https://stormy-everglades-69504.herokuapp.com/recipes";
        RequestHandler.getInstance(getApplicationContext()).handlePOSTRequest(apiRecipesResourseUrl, null, responseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                JSONObject RecipeJsonObject = new JSONObject(responseArray[0]);
                int recipeId = Integer.parseInt(RecipeJsonObject.getString("recipe_id"));
                startEditRecipeActivity(recipeId);
                return null;
            }
        });
    }
    private void startEditRecipeActivity(int recipeId) {
        Intent intent = EditRecipeActivity.newIntent(HomeActivity.this, recipeId);
        startActivity(intent);
    }
     private void postNewShoppingList() {
        final String[] responseArray = new String[1];
        String apiShoppingListsResourceUrl = "https://stormy-everglades-69504.herokuapp.com/groceries";
        RequestHandler.getInstance(getApplicationContext()).handlePOSTRequest(apiShoppingListsResourceUrl, null, responseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                JSONObject ShoppingListJsonObject = new JSONObject(responseArray[0]);
                int shoppingListId = Integer.parseInt(ShoppingListJsonObject.getString("list_id"));
                startEditShoppingListActivity(shoppingListId);
                return null;
            }
        });
    }
    private void startEditShoppingListActivity(int shoppingListId) {
        Intent intent = EditShoppingListActivity.newIntent(HomeActivity.this, shoppingListId);
        startActivity(intent);
    }
}
