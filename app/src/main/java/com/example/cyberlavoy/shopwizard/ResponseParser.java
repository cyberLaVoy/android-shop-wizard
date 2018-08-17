package com.example.cyberlavoy.shopwizard;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CyberLaVoy on 8/12/2018.
 */

public class ResponseParser {
    private static final String TAG = "ResponseParser";
    private Context mContext;

    public ResponseParser(Context context) {
        mContext = context;
    }

    public void parseItemsJSONString(String jsonString) {
        try {
            JSONObject itemsObject = new JSONObject(jsonString);
            JSONArray itemsArray = itemsObject.getJSONArray("ingredients");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                int itemId = itemObject.getInt("ingredient_id");
                String itemLabel = itemObject.getString("label");
                String itemCategory = itemObject.getString("category");
                Item item = new Item(itemId, itemLabel, itemCategory);
                ListStore listStore = ListStore.getInstance(mContext);
                if (! listStore.getItems().contains(item)) {
                    listStore.addItem(item);
                }
            }
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }
    }
    public void parseRecipesJSONString(String jsonString) {
        try {
            JSONObject recipesObject = new JSONObject(jsonString);
            JSONArray itemsArray = recipesObject.getJSONArray("recipes");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject recipeObject = itemsArray.getJSONObject(i);
                int recipeId = recipeObject.getInt("recipe_id");
                String recipeLabel = recipeObject.getString("label");
                String recipeInstructions = recipeObject.getString("instructions");
                Recipe recipe = new Recipe(recipeId, recipeLabel, recipeInstructions);
                JSONArray recipeIngredients = recipeObject.getJSONArray("ingredients");
                for (int j = 0; j < recipeIngredients.length(); j++) {
                    JSONObject ingredient = recipeIngredients.getJSONObject(j);
                    int ingredientId = ingredient.getInt("ingredient_id");
                    String ingredientLabel = ingredient.getString("label");
                    String category = ingredient.getString("category");
                    String quantity = ingredient.getString("quantity");
                    String quantityType = ingredient.getString("quantity_type");
                    RecipeIngredient recipeIngredient = new RecipeIngredient(ingredientId, ingredientLabel, category, quantity, quantityType);
                    recipe.addItem(recipeIngredient);
                }
                ListStore listStore = ListStore.getInstance(mContext);
                if (! listStore.getRecipes().contains(recipe)) {
                    listStore.addRecipe(recipe);
                }
            }
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }
    }
    public Recipe parseRecipeJSONString(String jsonString) throws JSONException {
        JSONObject recipeObject = new JSONObject(jsonString);
        int recipeId = recipeObject.getInt("recipe_id");
        String recipeLabel = recipeObject.getString("label");
        String recipeInstructions = recipeObject.getString("instructions");
        Recipe recipe = new Recipe(recipeId, recipeLabel, recipeInstructions);
        JSONArray recipeIngredients = recipeObject.getJSONArray("ingredients");
        for (int j = 0; j < recipeIngredients.length(); j++) {
            JSONObject ingredient = recipeIngredients.getJSONObject(j);
            int ingredientId = ingredient.getInt("ingredient_id");
            String ingredientLabel = ingredient.getString("label");
            String category = ingredient.getString("category");
            String quantity = ingredient.getString("quantity");
            String quantityType = ingredient.getString("quantity_type");
            RecipeIngredient recipeIngredient = new RecipeIngredient(ingredientId, ingredientLabel, category, quantity, quantityType);
            recipe.addItem(recipeIngredient);
        }
        ListStore listStore = ListStore.getInstance(mContext);
        if (! listStore.getRecipes().contains(recipe)) {
            listStore.addRecipe(recipe);
        }
        return recipe;
    }
    public void parseShoppingListsJSONString(String jsonString) {
        try {
            JSONObject shoppingListsObject = new JSONObject(jsonString);
            JSONArray shoppingListsArray = shoppingListsObject.getJSONArray("grocery_lists");
            for (int i = 0; i < shoppingListsArray.length(); i++) {
                String shoppingList = shoppingListsArray.getString(i);
                parseShoppingListJSONString(shoppingList);
            }
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }
    }
    public ShoppingList parseShoppingListJSONString(String jsonString) throws JSONException {
        JSONObject shoppingListObject = new JSONObject(jsonString);
        int shoppingListId = shoppingListObject.getInt("list_id");
        String shoppingListLabel = shoppingListObject.getString("label");
        ShoppingList shoppingList = new ShoppingList(shoppingListId, shoppingListLabel);
        JSONArray items = shoppingListObject.getJSONArray("items");
        for (int j = 0; j < items.length(); j++) {
            String item = items.getString(j);
            ShoppingItem shoppingItem = parseShoppingListItemJsonString(item);
            shoppingList.addItem(shoppingItem);
        }
        ListStore listStore = ListStore.getInstance(mContext);
        if (! listStore.getShoppingLists().contains(shoppingList)) {
            listStore.addShoppingList(shoppingList);
        }
        return shoppingList;
    }
    public ShoppingItem parseShoppingListItemJsonString(String itemJsonString) throws JSONException {
        JSONObject item = new JSONObject(itemJsonString);
        int itemId = item.getInt("ingredient_id");
        String itemLabel = item.getString("label");
        String category = item.getString("category");
        String quantity = item.getString("quantity");
        String quantityType = item.getString("quantity_type");
        int numRecipesReferenced = item.getInt("num_recipes_referenced");
        boolean grabbed = item.getBoolean("grabbed");
        ShoppingItem shoppingItem = new ShoppingItem(itemId, itemLabel, category, quantity, quantityType, numRecipesReferenced, grabbed);
        return shoppingItem;
    }
}
