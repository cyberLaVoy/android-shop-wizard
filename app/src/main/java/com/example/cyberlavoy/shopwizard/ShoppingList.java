package com.example.cyberlavoy.shopwizard;

/**
 * Created by CyberLaVoy on 8/11/2018.
 */

public class ShoppingList extends ItemList {

    public ShoppingList(int shoppingListId, String label) {
        super(shoppingListId, label);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ShoppingList)) {
            return false;
        }
        ShoppingList other = (ShoppingList) obj;
        return this.getListId() == other.getListId();
    }
}
