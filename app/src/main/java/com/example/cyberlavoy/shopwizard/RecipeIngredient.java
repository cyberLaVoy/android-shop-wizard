package com.example.cyberlavoy.shopwizard;

/**
 * Created by CyberLaVoy on 8/11/2018.
 */

public class RecipeIngredient extends Item {
    private String mQuantity;
    private String mQuantityType;

    public RecipeIngredient(int itemId, String label, String category, String quantity, String quantityType) {
        super(itemId, label, category);
        setQuantity(quantity);
        setQuantityType(quantityType);
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }

    public String getQuantityType() {
        return mQuantityType;
    }

    public void setQuantityType(String quantityType) {
        mQuantityType = quantityType;
    }
}
