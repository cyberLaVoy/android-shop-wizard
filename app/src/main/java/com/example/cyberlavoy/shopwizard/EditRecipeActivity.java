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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class EditRecipeActivity extends AppCompatActivity {
    private static final String EXTRA_RECIPE_ID= "com.example.cyberlavoy.shopwizard.extra_shopping_list_id";
    private static final String SAVED_RECIPE_ID= "com.example.cyberlavoy.shopwizard.saved_shopping_list_id";
    private String mApiRecipesResourseUrl = "https://stormy-everglades-69504.herokuapp.com/recipes";
    private String mApiRecipeIngredientsResourseUrl = "https://stormy-everglades-69504.herokuapp.com/recipes/ingredients";
    private int mRecipeId;
    private Recipe mRecipe;
    private LinearLayout mIngredientsListView;
    private EditText mDirectionsView;
    private EditText mRecipeLabelEditText;
    private ArrayList<View> mIngredientsViewList;
    private FloatingActionButton mMenuDisplayFloatingActionBtn;
    private ConstraintLayout mOptionsMenu;
    private TextView mAddItemsMenuOption;
    private int mAddItemsResultCode = 1;

    public static Intent newIntent(Context packageContext, int recipeListId) {
        Intent intent = new Intent(packageContext, EditRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeListId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        if (recipeId != -1) {
            mRecipeId = recipeId;
        }
        else {
            mRecipeId = savedInstanceState.getInt(SAVED_RECIPE_ID);
        }
        mRecipeLabelEditText = findViewById(R.id.edit_recipe_recipe_label);
        mIngredientsListView = findViewById(R.id.recipe_ingredients_view);
        mDirectionsView = findViewById(R.id.recipe_directions_view);
        mMenuDisplayFloatingActionBtn = findViewById(R.id.edit_recipe_floating_action_btn);
        mOptionsMenu = findViewById(R.id.edit_recipe_options_menu);
        mAddItemsMenuOption = findViewById(R.id.edit_recipe_add_items_option);
        updateUI();
        mMenuDisplayFloatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleOptionsMenu();
            }
        });
        mAddItemsMenuOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = ItemsActivity.newIntent(EditRecipeActivity.this, true);
               startActivityForResult(intent, mAddItemsResultCode);
            }
        });
        mRecipeLabelEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (mRecipe != null) {
                    String newLabel = mRecipeLabelEditText.getText().toString();
                    if (newLabel.equals("")) {
                        newLabel = " ";
                    }
                        updateRecipeLabel(newLabel);
                }
            }
        });
        mDirectionsView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (mRecipe != null) {
                    String newDirections = mDirectionsView.getText().toString();
                    if (newDirections.equals("")) {
                        newDirections = " ";
                    }
                        updateRecipeInstructions(newDirections);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_recipe_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_recipe_delete_recipe_menu_btn:
                deleteRecipe();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
      @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mAddItemsResultCode) {
            if(resultCode == Activity.RESULT_OK){
                String selectedItemsIds = data.getStringExtra("result");
                try {
                    JSONArray itemsIdsJsonArray = new JSONArray(selectedItemsIds);
                    addItemsToRecipe(itemsIdsJsonArray);
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
        outState.putInt(SAVED_RECIPE_ID, mRecipeId);
    }

    private void updateRecipeLabel(String label) {
        ListStore.getInstance(getApplicationContext()).getRecipe(mRecipeId).setLabel(label);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("label", label);
        RequestHandler.getInstance(getApplicationContext()).handlePUTRequest(mApiRecipesResourseUrl + "/" + Integer.toString(mRecipeId), requestBody, null, null);
    }
     private void updateRecipeInstructions(String instructions) {
        if (!instructions.equals(mRecipe.getInstructions())) {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("instructions", instructions);
            RequestHandler.getInstance(getApplicationContext()).handlePUTRequest(mApiRecipesResourseUrl + "/" + Integer.toString(mRecipeId), requestBody, null, null);
        }
    }
    private void updateRecipeIngredientQuantity(String quantity, RecipeIngredient recipeIngredient) {
        if (!quantity.equals(recipeIngredient.getQuantity())) {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("quantity", quantity);
            requestBody.put("ingredient_id", Integer.toString(recipeIngredient.getItemId()));
            requestBody.put("recipe_id", Integer.toString(mRecipeId));
            RequestHandler.getInstance(getApplicationContext()).handlePUTRequest(mApiRecipeIngredientsResourseUrl, requestBody, null, null);
        }
    }
     private void updateRecipeIngredientQuantityType(String quantityType, RecipeIngredient recipeIngredient) {
        if (!quantityType.equals(recipeIngredient.getQuantityType())) {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("quantity_type", quantityType);
            requestBody.put("ingredient_id", Integer.toString(recipeIngredient.getItemId()));
            requestBody.put("recipe_id", Integer.toString(mRecipeId));
            RequestHandler.getInstance(getApplicationContext()).handlePUTRequest(mApiRecipeIngredientsResourseUrl, requestBody, null, null);
        }
    }
    private void deleteRecipeIngredient(int ingredientId, View recipeIngredientView) {
        removeRecipeIngredientView(recipeIngredientView);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("recipe_id", Integer.toString(mRecipeId));
        requestBody.put("ingredient_id", Integer.toString(ingredientId));
        RequestHandler requestHandler = RequestHandler.getInstance(getApplicationContext());
        String queryString = '?' + requestHandler.mapBodyToQS(requestBody);
        requestHandler.handleDELETERequest(mApiRecipeIngredientsResourseUrl + queryString, null, null, null);
    }
     private void updateUI() {
        clearIngredientsList();
        final String[] responseArray = new String[1];
        RequestHandler.getInstance(getApplicationContext()).handleGETRequest(mApiRecipesResourseUrl + "/" + Integer.toString(mRecipeId), responseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                String responseJson = responseArray[0];
                ResponseParser responseParser = new ResponseParser(getApplicationContext());
                ListStore.getInstance(getApplicationContext()).removeRecipe(mRecipeId);
                mRecipe = responseParser.parseRecipeJSONString(responseJson);
                displayRecipe();
                return null;
            }
        });
    }
    private void clearIngredientsList() {
       mIngredientsViewList = new ArrayList<>();
       mIngredientsListView.removeAllViews();
    }
    private void displayRecipe() {
        mRecipeLabelEditText.setText(mRecipe.getLabel());
        ArrayList<RecipeIngredient> recipeIngredients = mRecipe.getItems();
        String recipeDirections = mRecipe.getInstructions();
        mDirectionsView.setText(recipeDirections);
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            View recipeIngredientView = LayoutInflater.from(EditRecipeActivity.this)
                    .inflate(R.layout.recipe_item_view, mIngredientsListView, false);
            updateRecipeIngredientView(recipeIngredient, recipeIngredientView);
            addRecipeIngredientView(recipeIngredientView);
        }
    }
    private void updateRecipeIngredientView(final RecipeIngredient recipeIngredient, final View recipeIngredientView) {
        TextView recipeIngredientLabelView = recipeIngredientView.findViewById(R.id.recipe_item_label);
        EditText recipeIngredientQuantityView = recipeIngredientView.findViewById(R.id.recipe_item_quantity);
        EditText recipeIngredientQuantityTypeView = recipeIngredientView.findViewById(R.id.recipe_item_quantity_type);
        recipeIngredientLabelView.setText(recipeIngredient.getLabel());
        recipeIngredientQuantityView.setText(recipeIngredient.getQuantity());
        recipeIngredientQuantityTypeView.setText(recipeIngredient.getQuantityType());
        ImageButton deleteIngredientBtn = recipeIngredientView.findViewById(R.id.edit_recipe_delete_item_btn);
        deleteIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecipeIngredient(recipeIngredient.getItemId(), recipeIngredientView);
            }
        });
        recipeIngredientQuantityView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateRecipeIngredientQuantity(charSequence.toString(), recipeIngredient);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        recipeIngredientQuantityTypeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateRecipeIngredientQuantityType(charSequence.toString(), recipeIngredient);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void addRecipeIngredientView(View recipeIngredientView) {
        mIngredientsViewList.add(recipeIngredientView);
        mIngredientsListView.addView(recipeIngredientView);
    }
    private void removeRecipeIngredientView(View recipeIngredientView) {
        mIngredientsViewList.remove(recipeIngredientView);
        mIngredientsListView.removeView(recipeIngredientView);
    }
    private void deleteRecipe() {
        mRecipe = null;
        ListStore.getInstance(getApplicationContext()).removeRecipe(mRecipeId);
        RequestHandler.getInstance(getApplicationContext()).handleDELETERequest(mApiRecipesResourseUrl + "/" + Integer.toString(mRecipeId),null,null,null);
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

    private void addItemsToRecipe(JSONArray itemsIds) throws JSONException {
        for (int i = 0; i < itemsIds.length(); i++) {
            int itemId = itemsIds.getInt(i);
            addNewItemToRecipe(itemId, i == itemsIds.length()-1);
        }
    }
    private void addNewItemToRecipe(int itemId, final boolean updateUI) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("recipe_id", Integer.toString(mRecipeId));
        requestBody.put("ingredient_id", Integer.toString(itemId));
        RequestHandler.getInstance(getApplicationContext()).handlePOSTRequest(mApiRecipeIngredientsResourseUrl, requestBody, null, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (updateUI) {
                    updateUI();
                }
                return null;
            }
        });
    }

}
