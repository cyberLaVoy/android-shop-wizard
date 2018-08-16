package com.example.cyberlavoy.shopwizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button mShoppingListsButton;
    private Button mRecipesButton;
    private Button mItemsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mShoppingListsButton = findViewById(R.id.home_shopping_lists_btn);
        mRecipesButton = findViewById(R.id.home_recipes_btn);
        mItemsButton = findViewById(R.id.home_items_btn);

        mShoppingListsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ShoppingListsActivity.newIntent(HomeActivity.this);
                startActivity(intent);
            }
        });
        mRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RecipesActivity.newIntent(HomeActivity.this, false);
                startActivity(intent);
            }
        });
        mItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ItemsActivity.newIntent(HomeActivity.this, false);
                startActivity(intent);
            }
        });
    }
}
