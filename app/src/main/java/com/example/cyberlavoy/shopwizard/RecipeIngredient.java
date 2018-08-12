package com.example.cyberlavoy.shopwizard;

/**
 * Created by CyberLaVoy on 8/11/2018.
 */

public class RecipeIngredient {
    private Item mItem;
    private String mQuantity;
    private String mQuantityType;

    public RecipeIngredient(Item item, String quantity, String quantityType) {
        mItem = item;
        mQuantity = quantity;
        mQuantityType = quantityType;
    }

    public Item getItem() {
        return mItem;
    }

    public void setItem(Item item) {
        mItem = item;
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
