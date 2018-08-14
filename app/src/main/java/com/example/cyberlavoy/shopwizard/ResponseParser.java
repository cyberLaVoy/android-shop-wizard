package com.example.cyberlavoy.shopwizard;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CyberLaVoy on 8/12/2018.
 */

public class ResponseParser {
    private static final String TAG = "ResponseParser";
    private Context mContext;

    public ResponseParser(Context context) {
        mContext = context;
    }

    public void parseItemsJSONString(String jsonString) {
        try {
            JSONObject itemsObject = new JSONObject(jsonString);
            JSONArray itemsArray = itemsObject.getJSONArray("ingredients");
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                int itemId = (int) itemObject.get("ingredient_id");
                String itemLabel = (String) itemObject.get("label");
                String itemCategory = (String) itemObject.get("category");
                Item item = new Item(itemId, itemLabel, itemCategory);
                ListStore listStore = ListStore.getInstance(mContext);
                if (! listStore.getItems().contains(item)) {
                    listStore.addItem(item);
                }
            }

        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }
    }
}
