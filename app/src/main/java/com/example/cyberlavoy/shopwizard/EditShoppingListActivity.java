package com.example.cyberlavoy.shopwizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private int mShoppingListId;
    private ShoppingList mShoppingList;
    private int mAddItemsResultCode = 1;
    private int mAddRecipesResultCode = 2;
    private EditText mShoppingListLabelEditText;
    private FloatingActionButton mMenuDisplayFloatingActionBtn;
    private ConstraintLayout mOptionsMenu;
    private TextView mAddRecipesMenuOption;
    private TextView mAddItemsMenuOption;
    private LinearLayout mShoppingListLayout;
    private List<ShoppingItem> mShoppingListItems;
    private Map<String, LinearLayout> mCategoriesLayoutMap;

    public static Intent newIntent(Context packageContext, int shoppingListId) {
        Intent intent = new Intent(packageContext, EditShoppingListActivity.class);
        intent.putExtra(EXTRA_SHOPPING_LIST_ID, shoppingListId);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_shopping_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_shopping_list_delete_list_menu_btn:
                deleteShoppingList();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shopping_list);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int shoppingListId = getIntent().getIntExtra(EXTRA_SHOPPING_LIST_ID, -1);
        if (shoppingListId != -1) {
            mShoppingListId = shoppingListId;
        }
        else {
            mShoppingListId = savedInstanceState.getInt(EXTRA_SHOPPING_LIST_ID);
        }
        mShoppingListLabelEditText = findViewById(R.id.edit_shopping_list_list_label);
        mShoppingListLayout = findViewById(R.id.shopping_list_view);
        mMenuDisplayFloatingActionBtn = findViewById(R.id.shopping_list_view_foating_action_btn);
        mAddRecipesMenuOption = findViewById(R.id.shopping_lists_add_recipes_option);
        mAddItemsMenuOption = findViewById(R.id.shopping_lists_add_items_option);
        mOptionsMenu = findViewById(R.id.shopping_lists_options_menu);
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
         mShoppingListLabelEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (mShoppingList != null) {
                    String newLabel = mShoppingListLabelEditText.getText().toString();
                    if (newLabel.equals("")) {
                        newLabel = " ";
                    }
                    updateShoppingListLabel(newLabel);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        getCurrentFocus().clearFocus();
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(EXTRA_SHOPPING_LIST_ID, mShoppingListId);
    }

    private void updateUI() {
        resetShoppingList();
        final String[] responseArray = new String[1];
        RequestHandler.getInstance(getApplicationContext()).handleGETRequest(apiShoppingListResourseUrl + "/" + Integer.toString(mShoppingListId), responseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                String responseJson = responseArray[0];
                ResponseParser responseParser = new ResponseParser(getApplicationContext());
                ListStore.getInstance(getApplicationContext()).removeShoppingList(mShoppingListId);
                mShoppingList = responseParser.parseShoppingListJSONString(responseJson);
                loadShoppingListItems();
                displayShoppingList();
                return null;
            }
        });
    }
    private void resetShoppingList() {
        mShoppingListLayout.removeAllViews();
        mCategoriesLayoutMap = new HashMap<>();
        mShoppingListItems = new ArrayList<>();
    }
    private void deleteShoppingList() {
        mShoppingList = null;
        ListStore.getInstance(getApplicationContext()).removeShoppingList(mShoppingListId);
        RequestHandler.getInstance(getApplicationContext()).handleDELETERequest(apiShoppingListResourseUrl + "/" + Integer.toString(mShoppingListId),null,null,null);
    }
    private void updateShoppingListLabel(String label) {
        ListStore.getInstance(getApplicationContext()).getShoppingList(mShoppingListId).setLabel(label);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("label", label);
        RequestHandler.getInstance(getApplicationContext()).handlePUTRequest(apiShoppingListResourseUrl + "/" + Integer.toString(mShoppingListId), requestBody, null, null);
    }
    private void addItemsToShoppingList(JSONArray itemsIds) throws JSONException {
        for (int i = 0; i < itemsIds.length(); i++) {
            int itemId = itemsIds.getInt(i);
            addNewItemToShoppingList(itemId, i == itemsIds.length()-1);
        }
    }
    private void addNewItemToShoppingList(int itemId, final boolean updateUI) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("list_id", Integer.toString(mShoppingListId));
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
        requestBody.put("list_id", Integer.toString(mShoppingListId));
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
        mShoppingListLabelEditText.setText(mShoppingList.getLabel());
        for (ShoppingItem shoppingItem: mShoppingListItems) {
            addItemToCategoryView(shoppingItem);
        }
    }
    private void addItemToCategoryView(ShoppingItem shoppingItem) {
        String category;
        if (shoppingItem.isGrabbed()) {
            category = "Grabbed";
        }
        else {
            category = shoppingItem.getCategory();
        }
        View shoppingListItemView = LayoutInflater.from(EditShoppingListActivity.this)
                .inflate(R.layout.shopping_list_item_view, mCategoriesLayoutMap.get(category), false);
        updateItemView(shoppingListItemView, shoppingItem);
        addItemViewToCategoryLayout(shoppingListItemView, category);
    }
    private void updateItemView(final View shoppingListItemView, final ShoppingItem shoppingItem) {
        TextView itemLabelTextView = shoppingListItemView.findViewById(R.id.shopping_list_item_label);
        TextView itemRecipesReferencedTextView = shoppingListItemView.findViewById(R.id.num_recipes_referenced);
        final EditText itemQuantityEditTextView = shoppingListItemView.findViewById(R.id.shopping_list_item_quantity);
        final EditText itemQuantityTypeEditTextView = shoppingListItemView.findViewById(R.id.shopping_list_item_quantity_type);
        CheckBox itemGrabbedCheckBox = shoppingListItemView.findViewById(R.id.shopping_list_item_check_box);
        ImageButton deleteItemBtn = shoppingListItemView.findViewById(R.id.edit_shopping_list_delete_item_btn);
        itemLabelTextView.setText(shoppingItem.getLabel());
        itemRecipesReferencedTextView.setText(Integer.toString(shoppingItem.getNumRecipesReferenced()) + " recipes");
        itemQuantityEditTextView.setText(shoppingItem.getQuantity());
        itemQuantityTypeEditTextView.setText(shoppingItem.getQuantityType());
        if (shoppingItem.isGrabbed()) {
            itemGrabbedCheckBox.setChecked(true);
        }
        itemGrabbedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // if item is grabbed, move to grabbed
                if (b) {
                    moveItemToDifferentCategory(shoppingItem.getCategory(), "Grabbed", shoppingListItemView);
                }
                // else move back to original category
                else {
                    moveItemToDifferentCategory("Grabbed", shoppingItem.getCategory(), shoppingListItemView);
                }
                shoppingItem.setGrabbed(b);
                setShoppingListItemGrabbed(b, shoppingItem);
            }
        });
        deleteItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteShoppingListItem(shoppingItem);
                removeItemViewFromLayout(shoppingListItemView, shoppingItem);
            }
        });
        itemQuantityEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateShoppingItemQuantity(charSequence.toString(), shoppingItem);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        itemQuantityTypeEditTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String newQuantityType = itemQuantityTypeEditTextView.getText().toString();
                    updateShoppingItemQuantityType(newQuantityType, shoppingItem, shoppingListItemView);
                }
            }
        });
    }
    private void addItemViewToCategoryLayout(View shoppingListItemView, String category) {
        mCategoriesLayoutMap.get(category).addView(shoppingListItemView);
    }
    private void removeItemViewFromLayout(View shoppingListItemView, ShoppingItem shoppingItem) {
        String itemCateogry = shoppingItem.getCategory();
        if (shoppingItem.isGrabbed()) {
            mCategoriesLayoutMap.get("Grabbed").removeView(shoppingListItemView);
        }
        else {
            mCategoriesLayoutMap.get(itemCateogry).removeView(shoppingListItemView);
        }
    }
    private void loadShoppingListItems(){
        ShoppingList shoppingList = ListStore.getInstance(getApplicationContext()).getShoppingList(mShoppingListId);
        ArrayList<ShoppingItem> shoppingItems = shoppingList.getItems();
        String itemCategory;
        for (ShoppingItem shoppingItem: shoppingItems) {
            itemCategory = shoppingItem.getCategory();
            if (!mCategoriesLayoutMap.containsKey(itemCategory)) {
                createNewCategory(itemCategory);
            }
            mShoppingListItems.add(shoppingItem);
        }
        createNewCategory("Grabbed");
    }
    private void createNewCategory(String itemCategory) {
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
    private void moveItemToDifferentCategory(String currentCategory, String newCategory, View itemView) {
        if (mCategoriesLayoutMap.containsKey(currentCategory) && mCategoriesLayoutMap.containsKey(newCategory)) {
            mCategoriesLayoutMap.get(currentCategory).removeView(itemView);
            mCategoriesLayoutMap.get(newCategory).addView(itemView);
        }
    }
    private void setShoppingListItemGrabbed(boolean grabbed, ShoppingItem shoppingItem) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grabbed", Boolean.toString(grabbed));
        requestBody.put("ingredient_id", Integer.toString(shoppingItem.getItemId()));
        requestBody.put("list_id", Integer.toString(mShoppingListId));
        requestBody.put("original_quantity_type", shoppingItem.getQuantityType());
        RequestHandler.getInstance(getApplicationContext()).handlePUTRequest(apiShoppingItemsResourseUrl, requestBody, null, null);
    }
    private void deleteShoppingListItem(ShoppingItem shoppingItem) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("ingredient_id", Integer.toString(shoppingItem.getItemId()));
        requestBody.put("list_id", Integer.toString(mShoppingListId));
        requestBody.put("quantity_type", shoppingItem.getQuantityType());
        RequestHandler requestHandler = RequestHandler.getInstance(getApplicationContext());
        String queryString = '?' + requestHandler.mapBodyToQS(requestBody);
        requestHandler.handleDELETERequest(apiShoppingItemsResourseUrl + queryString, null, null, null);
    }
     private void updateShoppingItemQuantity(String quantity, ShoppingItem shoppingItem) {
        if (!quantity.equals(shoppingItem.getQuantity())) {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("ingredient_id", Integer.toString(shoppingItem.getItemId()));
            requestBody.put("list_id", Integer.toString(mShoppingListId));
            requestBody.put("original_quantity_type", shoppingItem.getQuantityType());
            requestBody.put("quantity", quantity);
            RequestHandler.getInstance(getApplicationContext()).handlePUTRequest(apiShoppingItemsResourseUrl, requestBody, null, null);
        }
    }
     private void updateShoppingItemQuantityType(String newQuantityType, final ShoppingItem shoppingItem, final View shoppingItemView) {
        if (!newQuantityType.equals(shoppingItem.getQuantityType())) {
            final String[] responseArray = new String[1];
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("ingredient_id", Integer.toString(shoppingItem.getItemId()));
            requestBody.put("list_id", Integer.toString(mShoppingListId));
            requestBody.put("original_quantity_type", shoppingItem.getQuantityType());
            shoppingItem.setQuantityType(newQuantityType);
            requestBody.put("new_quantity_type", newQuantityType);
            RequestHandler.getInstance(getApplicationContext()).handlePUTRequest(apiShoppingItemsResourseUrl, requestBody, responseArray, new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    ListStore.getInstance(getApplicationContext()).getShoppingList(mShoppingListId).deleteItem(shoppingItem.getItemId());
                    ResponseParser responseParser = new ResponseParser(getApplicationContext());
                    ShoppingItem newShoppingItem = responseParser.parseShoppingListItemJsonString(responseArray[0]);
                    removeDuplicateItemViews(newShoppingItem, shoppingItemView);
                    mCategoriesLayoutMap.get(newShoppingItem.getCategory()).removeView(shoppingItemView);
                    mCategoriesLayoutMap.get("Grabbed").removeView(shoppingItemView);
                    addItemToCategoryView(newShoppingItem);
                    return null;
                }
            });
        }
    }
    private void removeDuplicateItemViews(ShoppingItem shoppingItem, View shoppingItemView) {
        String category = shoppingItem.getCategory();
        String itemLabel = shoppingItem.getLabel();
        String itemQuantityType = shoppingItem.getQuantityType();
        LinearLayout categoryLayout = mCategoriesLayoutMap.get(category);
        for (int i = 1; i < categoryLayout.getChildCount(); i++) {
            View itemView = categoryLayout.getChildAt(i);
            TextView itemViewLabelView = itemView.findViewById(R.id.shopping_list_item_label);
            EditText itemViewQuantityTypeView = itemView.findViewById(R.id.shopping_list_item_quantity_type);
            String itemViewLabel = itemViewLabelView.getText().toString();
            String itemViewQuantityType = itemViewQuantityTypeView.getText().toString();
            if(itemLabel.equals(itemViewLabel) && itemQuantityType.equals(itemViewQuantityType)) {
                if (!shoppingItemView.equals(itemView)) {
                    categoryLayout.removeView(itemView);
                    break;
                }
            }
        }
        categoryLayout = mCategoriesLayoutMap.get("Grabbed");
        for (int i = 1; i < categoryLayout.getChildCount(); i++) {
            View itemView = categoryLayout.getChildAt(i);
            TextView itemViewLabelView = itemView.findViewById(R.id.shopping_list_item_label);
            EditText itemViewQuantityTypeView = itemView.findViewById(R.id.shopping_list_item_quantity_type);
            String itemViewLabel = itemViewLabelView.getText().toString();
            String itemViewQuantityType = itemViewQuantityTypeView.getText().toString();
            if(itemLabel.equals(itemViewLabel) && itemQuantityType.equals(itemViewQuantityType)) {
                if (!shoppingItemView.equals(itemView)) {
                    categoryLayout.removeView(itemView);
                    break;
                }
            }
        }
    }
}
