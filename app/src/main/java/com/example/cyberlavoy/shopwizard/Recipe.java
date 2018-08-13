package com.example.cyberlavoy.shopwizard;


/**
 * Created by CyberLaVoy on 8/11/2018.
 */

public class Recipe extends ItemList {
    private String mInstructions;

    public Recipe(int recipeId, String label, String instructions) {
        super(recipeId, label);
        setInstructions(instructions);
    }

    public String getInstructions() {
        return mInstructions;
    }

    public void setInstructions(String instructions) {
        mInstructions = instructions;
    }
}
