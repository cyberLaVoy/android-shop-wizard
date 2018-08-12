package com.example.cyberlavoy.shopwizard;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CyberLaVoy on 8/11/2018.
 */

public class ShoppingList {
    private int mShoppingListId;
    private String mLabel;
    private ArrayList mShoppingItems;

    public ShoppingList(int shoppingListId, String label) {
        mShoppingListId = shoppingListId;
        mLabel = label;
        mShoppingItems = new ArrayList<ShoppingItem>();
    }

    public void addItem(ShoppingItem shoppingItem) {
        mShoppingItems.add(shoppingItem);
    }
    public void deleteItem(int shoppingItemId) {
        for (Iterator<ShoppingItem> i = mShoppingItems.iterator(); i.hasNext();) {
            ShoppingItem shoppingItem = i.next();
            if (shoppingItem.getItem().getItemId() == shoppingItemId) {
                mShoppingItems.remove(shoppingItem);
            }
        }
    }
    public int getShoppingListId() {
        return mShoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        mShoppingListId = shoppingListId;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }
}
