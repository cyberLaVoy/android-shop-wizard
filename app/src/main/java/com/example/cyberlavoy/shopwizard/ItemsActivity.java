package com.example.cyberlavoy.shopwizard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ItemsActivity extends AppCompatActivity {

    private static final String EXTRA_ADDING_ITEMS_TO_LIST = "com.example.cyberlavoy.shopwizard.adding_items_to_list";
    private String apiUrl = "https://stormy-everglades-69504.herokuapp.com";
    private boolean mAddingItemsToList;
    RecyclerView mItemsRecyclerView;
    FloatingActionButton mFloatingActionButton;
    private ItemsAdapter mAdapter;

    public static Intent newIntent(Context packageContext, boolean addingItemsToList) {
        Intent intent = new Intent(packageContext, ItemsActivity.class);
        intent.putExtra(EXTRA_ADDING_ITEMS_TO_LIST, addingItemsToList);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        mAddingItemsToList = getIntent().getBooleanExtra(EXTRA_ADDING_ITEMS_TO_LIST, false);
        mFloatingActionButton = findViewById(R.id.add_item_floating_action_btn);
        mItemsRecyclerView = findViewById(R.id.items_recycler_view);
        mItemsRecyclerView.setLayoutManager(new LinearLayoutManager(ItemsActivity.this));
        updateUI();
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog(ItemsActivity.this);
            }
        });
    }
    private void showAddItemDialog(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Add a new item type")
                .setView(R.layout.add_new_item_type_dialog)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText newItemTypeLabel = ((AlertDialog) dialog).findViewById(R.id.new_item_type_label_input);
                        EditText newItemTypeCategory = ((AlertDialog) dialog).findViewById(R.id.new_item_type_category_input);
                        String label = newItemTypeLabel.getText().toString();
                        String category = newItemTypeCategory.getText().toString();
                        postNewItemType(label, category);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
    private void postNewItemType(String label, String category) {
        final Item newItem = new Item(-1, label, category);
        ListStore.getInstance(getApplicationContext()).addItem(newItem);
        updateUI();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("label", label);
        requestBody.put("category", category);
        final String[] responseArray = new String[1];
        RequestHandler.getInstance(getApplicationContext()).handlePOSTRequest(apiUrl + "/ingredients", requestBody, responseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                JSONObject itemJsonObject = new JSONObject(responseArray[0]);
                int itemId = Integer.parseInt(itemJsonObject.getString("ingredient_id"));
                newItem.setItemId(itemId);
                return null;
            }
        });
    }

    private void updateUI() {
        List<Item> items = ListStore.getInstance(getApplicationContext()).getItems();
        if (mAdapter == null) {
            mAdapter = new ItemsAdapter(items);
            mItemsRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Item mItem;
        TextView mItemLabelTextView;
        TextView mItemCategoryTextView;
        ImageButton mDeleteItemBtn;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mItemLabelTextView = itemView.findViewById(R.id.item_list_item_label);
            mItemCategoryTextView = itemView.findViewById(R.id.item_list_item_category);
            mDeleteItemBtn = itemView.findViewById(R.id.delete_item_btn);
            itemView.setOnClickListener(this);
        }

        public void bind(Item item) {
            mItem = item;
            mItemLabelTextView.setText(item.getLabel());
            mItemCategoryTextView.setText(item.getCategory());
            mDeleteItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ItemsActivity.this, Integer.toString(mItem.getItemId()), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemHolder> {
        List<Item> mItems;
        private ItemsAdapter(List<Item> items) {
            mItems = items;
        }
        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_item, parent, false);
            return new ItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            holder.bind(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void setItems(List<Item> items) {
            mItems = items;
        }
    }


}
