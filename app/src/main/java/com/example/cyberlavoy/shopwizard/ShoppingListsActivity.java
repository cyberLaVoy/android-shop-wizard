package com.example.cyberlavoy.shopwizard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Callable;

public class ShoppingListsActivity extends AppCompatActivity {
    private String apiShoppingListsResourceUrl = "https://stormy-everglades-69504.herokuapp.com/groceries";
    RecyclerView mShoppingListsRecyclerView;
    private ShoppingListsAdapter mAdapter;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ShoppingListsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists);
        mShoppingListsRecyclerView = findViewById(R.id.shopping_lists_recycler_view);
        mShoppingListsRecyclerView.setLayoutManager(new LinearLayoutManager(ShoppingListsActivity.this));
        updateUI();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopping_lists_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_shopping_list_menu_btn:
                postNewShoppingList();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void postNewShoppingList() {
        final String[] responseArray = new String[1];
        RequestHandler.getInstance(getApplicationContext()).handlePOSTRequest(apiShoppingListsResourceUrl, null, responseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                JSONObject ShoppingListJsonObject = new JSONObject(responseArray[0]);
                int shoppingListId = Integer.parseInt(ShoppingListJsonObject.getString("list_id"));
                startEditShoppingListActity(shoppingListId);
                return null;
            }
        });
    }
    private void startEditShoppingListActity(int shoppingListId) {
        Intent intent = EditShoppingListActivity.newIntent(ShoppingListsActivity.this, shoppingListId);
        startActivity(intent);
    }
    private void updateUI() {
        List<ShoppingList> ShoppingLists = ListStore.getInstance(getApplicationContext()).getShoppingLists();
        if (mAdapter == null) {
            mAdapter = new ShoppingListsAdapter(ShoppingLists);
            mShoppingListsRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setShoppingLists(ShoppingLists);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ShoppingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ShoppingList mShoppingList;
        TextView mShoppingListLabelTextView;

        public ShoppingListHolder(@NonNull View ShoppingListView) {
            super(ShoppingListView);
            mShoppingListLabelTextView = ShoppingListView.findViewById(R.id.shopping_list_recycler_view_label);
            ShoppingListView.setOnClickListener(this);
        }

        public void bind(ShoppingList ShoppingList) {
            mShoppingList = ShoppingList;
            mShoppingListLabelTextView.setText(ShoppingList.getLabel());
        }

        @Override
        public void onClick(View view) {
            Intent intent = EditShoppingListActivity.newIntent(ShoppingListsActivity.this, mShoppingList.getListId());
            startActivity(intent);
        }
    }

    private class ShoppingListsAdapter extends RecyclerView.Adapter<ShoppingListHolder> {
        List<ShoppingList> mShoppingLists;
        private ShoppingListsAdapter(List<ShoppingList> ShoppingLists) {
            mShoppingLists = ShoppingLists;
        }
        @NonNull
        @Override
        public ShoppingListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View ShoppingListView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_shopping_list_item, parent, false);
            return new ShoppingListHolder(ShoppingListView);
        }

        @Override
        public void onBindViewHolder(@NonNull ShoppingListHolder holder, int position) {
            holder.bind(mShoppingLists.get(position));
        }

        @Override
        public int getItemCount() {
            return mShoppingLists.size();
        }

        public void setShoppingLists(List<ShoppingList> ShoppingLists) {
            mShoppingLists = ShoppingLists;
        }
    }
}
