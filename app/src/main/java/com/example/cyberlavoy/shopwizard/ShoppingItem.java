package com.example.cyberlavoy.shopwizard;

/**
 * Created by CyberLaVoy on 8/11/2018.
 */

public class ShoppingItem {
    private Item mItem;
    private String mQuantity;
    private String mQuantityType;
    private int mNumRecipesReferenced;
    private boolean mGrabbed;

    public ShoppingItem(Item item, String quantity, String quantityType, int numRecipesReferenced, boolean grabbed) {
        mItem = item;
        mQuantity = quantity;
        mQuantityType = quantityType;
        mNumRecipesReferenced = numRecipesReferenced;
        mGrabbed = grabbed;
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

    public int getNumRecipesReferenced() {
        return mNumRecipesReferenced;
    }

    public void setNumRecipesReferenced(int numRecipesReferenced) {
        mNumRecipesReferenced = numRecipesReferenced;
    }

    public boolean isGrabbed() {
        return mGrabbed;
    }

    public void setGrabbed(boolean grabbed) {
        mGrabbed = grabbed;
    }
}
