package com.example.cyberlavoy.shopwizard;

/**
 * Created by CyberLaVoy on 8/11/2018.
 */

public class ShoppingItem extends Item {
    private String mQuantity;
    private String mQuantityType;
    private int mNumRecipesReferenced;
    private boolean mGrabbed;

    public ShoppingItem(int itemId, String label, String category, String quantity, String quantityType, int numRecipesReferenced, boolean grabbed) {
        super(itemId, label, category);
        setQuantity(quantity);
        setQuantityType(quantityType);
        setNumRecipesReferenced(numRecipesReferenced);
        setGrabbed(grabbed);
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
