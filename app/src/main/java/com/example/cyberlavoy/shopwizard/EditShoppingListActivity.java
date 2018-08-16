package com.example.cyberlavoy.shopwizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class EditShoppingListActivity extends AppCompatActivity {
    private static final String EXTRA_SHOPPING_LIST_ID= "com.example.cyberlavoy.shopwizard.shopping_list_id";
    private String apiShoppingItemsResourseUrl = "https://stormy-everglades-69504.herokuapp.com/groceries/items";
    private String apiShoppingRecipesResourseUrl = "https://stormy-everglades-69504.herokuapp.com/groceries/recipes";
    private String apiShoppingListResourseUrl = "https://stormy-everglades-69504.herokuapp.com/groceries";
    private int mShoppingListid;
    private int mAddItemsResultCode = 1;
    private int mAddRecipesResultCode = 2;
    private FloatingActionButton mMenuDisplayFloatingActionBtn;
    private ConstraintLayout mOptionsMenu;
    private TextView mAddRecipesMenuOption;
    private TextView mAddItemsMenuOption;
    private LinearLayout mShoppingListLayout;
    private List<ShoppingItem> mShoppingListItems;
    private Map<String, LinearLayout> mCategoriesLayoutMap;
    private Map<String, ArrayList<View>> mCategoriesShoppingItemsViewMap;

    public static Intent newIntent(Context packageContext, int shoppingListId) {
        Intent intent = new Intent(packageContext, EditShoppingListActivity.class);
        intent.putExtra(EXTRA_SHOPPING_LIST_ID, shoppingListId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shopping_list);
        mShoppingListLayout = findViewById(R.id.shopping_list_view);
        mMenuDisplayFloatingActionBtn = findViewById(R.id.shopping_list_view_foating_action_btn);
        mAddRecipesMenuOption = findViewById(R.id.shopping_lists_add_recipes_option);
        mAddItemsMenuOption = findViewById(R.id.shopping_lists_add_items_option);
        mOptionsMenu = findViewById(R.id.shopping_lists_options_menu);
        mShoppingListid = getIntent().getIntExtra(EXTRA_SHOPPING_LIST_ID, -1);
        updateUI();
        mMenuDisplayFloatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleOptionsMenu();
            }
        });
        mAddRecipesMenuOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = RecipesActivity.newIntent(EditShoppingListActivity.this, true);
               startActivityForResult(intent, mAddRecipesResultCode);
            }
        });
        mAddItemsMenuOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = ItemsActivity.newIntent(EditShoppingListActivity.this, true);
               startActivityForResult(intent, mAddItemsResultCode);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetShoppingList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mAddItemsResultCode) {
            if(resultCode == Activity.RESULT_OK){
                String selectedItemsIds = data.getStringExtra("result");
                try {
                    JSONArray itemsIdsJsonArray = new JSONArray(selectedItemsIds);
                    addItemsToShoppingList(itemsIdsJsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
        else if (requestCode == mAddRecipesResultCode) {
             if(resultCode == Activity.RESULT_OK){
                String selectedRecipesIds = data.getStringExtra("result");
                try {
                    JSONArray recipesIdsJsonArray = new JSONArray(selectedRecipesIds);
                    addRecipesToShoppingList(recipesIdsJsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }
    private void updateUI() {
        resetShoppingList();
        final String[] responseArray = new String[1];
        RequestHandler.getInstance(getApplicationContext()).handleGETRequest(apiShoppingListResourseUrl + "/" + Integer.toString(mShoppingListid), responseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                String responseJson = responseArray[0];
                ResponseParser responseParser = new ResponseParser(getApplicationContext());
                ListStore.getInstance(getApplicationContext()).removeShoppingList(mShoppingListid);
                responseParser.parseShoppingListJSONString(responseJson);
                loadShoppingListItems();
                displayShoppingList();
                return null;
            }
        });
    }
    private void resetShoppingList() {
        mShoppingListLayout.removeAllViews();
        mCategoriesLayoutMap = new HashMap<>();
        mCategoriesShoppingItemsViewMap = new HashMap<>();
        mShoppingListItems = new ArrayList<>();
    }

    private void addItemsToShoppingList(JSONArray itemsIds) throws JSONException {
        for (int i = 0; i < itemsIds.length(); i++) {
            int itemId = itemsIds.getInt(i);
            addNewItemToShoppingList(itemId, i == itemsIds.length()-1);
        }
    }
    private void addNewItemToShoppingList(int itemId, final boolean updateUI) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("list_id", Integer.toString(mShoppingListid));
        requestBody.put("ingredient_id", Integer.toString(itemId));
        RequestHandler.getInstance(getApplicationContext()).handlePOSTRequest(apiShoppingItemsResourseUrl, requestBody, null, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (updateUI) {
                    updateUI();
                }
                return null;
            }
        });
    }
    private void addRecipesToShoppingList(JSONArray recipesIds) throws JSONException {
        for (int i = 0; i < recipesIds.length(); i++) {
            int recipeId = recipesIds.getInt(i);
            addNewRecipeToShoppingList(recipeId, i == recipesIds.length()-1);
        }
    }
     private void addNewRecipeToShoppingList(int recipeId, final boolean updateUI) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("list_id", Integer.toString(mShoppingListid));
        requestBody.put("recipe_id", Integer.toString(recipeId));
        RequestHandler.getInstance(getApplicationContext()).handlePOSTRequest(apiShoppingRecipesResourseUrl, requestBody, null, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (updateUI) {
                    updateUI();
                }
                return null;
            }
        });
    }
    private void toggleOptionsMenu() {
        if (mOptionsMenu.getVisibility() == View.GONE) {
            mOptionsMenu.setVisibility(View.VISIBLE);
            mMenuDisplayFloatingActionBtn.setImageResource(R.drawable.ic_clear_black);
        }
        else {
            mOptionsMenu.setVisibility(View.GONE);
            mMenuDisplayFloatingActionBtn.setImageResource(R.drawable.ic_add_black);
        }
    }
    private void displayShoppingList() {
        for (ShoppingItem shoppingItem: mShoppingListItems) {
            addItemToCategoryView(shoppingItem);
        }
    }
    private void addItemToCategoryView(ShoppingItem shoppingItem) {
        String category = shoppingItem.getCategory();
        for (View view : mCategoriesShoppingItemsViewMap.get(category)) {
            TextView itemLabelTextView = view.findViewById(R.id.shopping_list_item_label);
            EditText itemQuantityTypeView = view.findViewById(R.id.shopping_list_item_quantity_type);
            if (itemLabelTextView.getText().equals(shoppingItem.getLabel()) && itemQuantityTypeView.getText().toString().equals(shoppingItem.getQuantityType())) {
                mCategoriesLayoutMap.get(category).removeView(view);
            }
        }
        View shoppingListItemView = LayoutInflater.from(EditShoppingListActivity.this)
                .inflate(R.layout.shopping_list_item_view, mCategoriesLayoutMap.get(category), false);
        TextView itemLabelTextView = shoppingListItemView.findViewById(R.id.shopping_list_item_label);
        itemLabelTextView.setText(shoppingItem.getLabel());
        TextView itemRecipesReferencedTextView = shoppingListItemView.findViewById(R.id.num_recipes_referenced);
        itemRecipesReferencedTextView.setText(Integer.toString(shoppingItem.getNumRecipesReferenced()) + " recipes");
        EditText itemQuantityEditTextView = shoppingListItemView.findViewById(R.id.shopping_list_item_quantity);
        itemQuantityEditTextView.setText(shoppingItem.getQuantity());
        EditText itemQuantityTypeEditTextView = shoppingListItemView.findViewById(R.id.shopping_list_item_quantity_type);
        itemQuantityTypeEditTextView.setText(shoppingItem.getQuantityType());
        mCategoriesLayoutMap.get(category).addView(shoppingListItemView);
        mCategoriesShoppingItemsViewMap.get(category).add(shoppingListItemView);
    }

    private void loadShoppingListItems(){
        ShoppingList shoppingList = ListStore.getInstance(getApplicationContext()).getShoppingList(mShoppingListid);
        ArrayList<ShoppingItem> shoppingItems = shoppingList.getItems();
        String itemCategory;
        for (ShoppingItem shoppingItem: shoppingItems) {
            itemCategory = shoppingItem.getCategory();
            if (!mCategoriesLayoutMap.containsKey(itemCategory)) {
                mCategoriesShoppingItemsViewMap.put(itemCategory, new ArrayList<View>());
                LinearLayout categoryLayout = new LinearLayout(EditShoppingListActivity.this);
                categoryLayout.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) );
                categoryLayout.setOrientation(LinearLayout.VERTICAL);
                View categoryHeaderView = LayoutInflater.from(EditShoppingListActivity.this)
                    .inflate(R.layout.category_header_view, categoryLayout, true);
                TextView categoryHeader = categoryHeaderView.findViewById(R.id.category_header);
                categoryHeader.setText(itemCategory);
                mCategoriesLayoutMap.put(itemCategory, categoryLayout);
                mShoppingListLayout.addView(categoryLayout);
            }
            mShoppingListItems.add(shoppingItem);
        }
    }
}
