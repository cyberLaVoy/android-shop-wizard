package com.example.cyberlavoy.shopwizard;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CyberLaVoy on 8/12/2018.
 */

public class ListStore {
    private static ListStore sListStore;
    private Context mContext;
    private ArrayList<Item> mItems;
    private ArrayList<ShoppingList> mShoppingLists;
    private ArrayList<Recipe> mRecipes;

    public static ListStore getInstance(Context context) {
        if (sListStore == null) {
            sListStore = new ListStore(context);
        }
        return sListStore;
    }

    private ListStore(Context context) {
        mContext = context.getApplicationContext();
        mItems = new ArrayList<>();
        mShoppingLists = new ArrayList<>();
        mRecipes = new ArrayList<>();
    }
    public void resetStore() {
        mItems = new ArrayList<>();
        mShoppingLists = new ArrayList<>();
        mRecipes = new ArrayList<>();
    }

    public void addItem(Item item) {
        mItems.add(item);
    }
    public void addRecipe(Recipe recipe) {
        mRecipes.add(recipe);
    }
    public void addShoppingList(ShoppingList shoppingList) {
        mShoppingLists.add(shoppingList);
    }

    public void removeItem(int itemId) {
        for (Iterator<Item> i = mItems.iterator(); i.hasNext();) {
            Item item = i.next();
            if (item.getItemId() == itemId) {
                mItems.remove(item);
                break;
            }
        }
    }
    public void removeRecipe(int recipeId) {
        for (Iterator<Recipe> i = mRecipes.iterator(); i.hasNext();) {
            Recipe recipe = i.next();
            if (recipe.getListId() == recipeId) {
                mRecipes.remove(recipe);
                break;
            }
        }
    }
    public void removeShoppingList(int shoppingListId) {
        for (Iterator<ShoppingList> i = mShoppingLists.iterator(); i.hasNext();) {
            ShoppingList shoppingList = i.next();
            if (shoppingList.getListId() == shoppingListId) {
                mShoppingLists.remove(shoppingList);
                break;
            }
        }
    }

    public ArrayList<Item> getItems() {
        return mItems;
    }

    public ArrayList<ShoppingList> getShoppingLists() {
        return mShoppingLists;
    }

    public ArrayList<Recipe> getRecipes() {
        return mRecipes;
    }

    public ShoppingList getShoppingList(int shoppingListId) {
        for (Iterator<ShoppingList> i = mShoppingLists.iterator(); i.hasNext();) {
            ShoppingList shoppingList = i.next();
            if (shoppingList.getListId() == shoppingListId) {
                return shoppingList;
            }
        }
        return null;
    }
}
