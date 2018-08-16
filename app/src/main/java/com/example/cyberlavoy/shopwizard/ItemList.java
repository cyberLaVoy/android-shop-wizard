package com.example.cyberlavoy.shopwizard;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CyberLaVoy on 8/12/2018.
 */

public class ItemList {
    private int mListId;
    private String mLabel;
    private ArrayList mItems;

    public ItemList(int listId, String label) {
        mListId = listId;
        mLabel = label;
        mItems = new ArrayList<Item>();
    }

    public void addItem(Item item) {
        mItems.add(item);
    }
    public void deleteItem(int itemId) {
        for (Iterator<Item> i = mItems.iterator(); i.hasNext();) {
            Item item = i.next();
            if (item.getItemId() == itemId) {
                mItems.remove(item);
            }
        }
    }

    public ArrayList getItems() {
        return mItems;
    }

    public int getListId() {
        return mListId;
    }

    public void setListId(int listId) {
        mListId = listId;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }
}
