package com.example.cyberlavoy.shopwizard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CyberLaVoy on 8/11/2018.
 */

public class Recipe {
    private int mRecipeId;
    private String mLabel;
    private String mInstructions;
    private ArrayList mIngredients;

    public Recipe(int recipeId, String label, String instructions) {
        mRecipeId = recipeId;
        mLabel = label;
        mInstructions = instructions;
        mIngredients = new ArrayList<RecipeIngredient>();
    }

    public void addIngredient(RecipeIngredient recipeIngredient) {
        mIngredients.add(recipeIngredient);
    }
    public void deleteIngredient(int ingredientId) {
        for (Iterator<RecipeIngredient> i = mIngredients.iterator(); i.hasNext();) {
            RecipeIngredient recipeIngredient = i.next();
            if (recipeIngredient.getItem().getItemId() == ingredientId) {
               mIngredients.remove(recipeIngredient);
            }
        }
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(int id) {
        mRecipeId = id;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getInstructions() {
        return mInstructions;
    }

    public void setInstructions(String instructions) {
        mInstructions = instructions;
    }
}
