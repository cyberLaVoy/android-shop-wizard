package com.example.cyberlavoy.shopwizard;

/**
 * Created by CyberLaVoy on 8/11/2018.
 */

public class Item {
    private int mItemId;
    private String mLabel;
    private String mCategory;

    public Item(int itemId, String label, String category) {
        setItemId(itemId);
        setLabel(label);
        setCategory(category);
    }

    public int getItemId() {
        return mItemId;
    }
    public void setItemId(int id) {
        mItemId = id;
    }
    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Item)) {
            return false;
        }
        Item other = (Item)obj;
        return this.mItemId == other.mItemId;
    }
}
