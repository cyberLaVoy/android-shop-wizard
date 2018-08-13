package com.example.cyberlavoy.shopwizard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemsActivity extends AppCompatActivity {

    private static final String EXTRA_ADDING_ITEMS_TO_LIST = "com.example.cyberlavoy.shopwizard.adding_items_to_list";
    private boolean mAddingItemsToList;
    RecyclerView mItemsRecyclerView;
    List<Item> mItems;

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
        mItems = ListStore.getInstance(getApplicationContext()).getItems();
        mItemsRecyclerView = findViewById(R.id.items_recycler_view);
        mItemsRecyclerView.setLayoutManager(new LinearLayoutManager(ItemsActivity.this));
        setupAdapter();
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Item mItem;
        TextView mItemLabelTextView;
        TextView mItemCategoryTextView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mItemLabelTextView = itemView.findViewById(R.id.item_list_item_label);
            mItemCategoryTextView = itemView.findViewById(R.id.item_list_item_category);
            itemView.setOnClickListener(this);
        }

        public void bind(Item item) {
            mItem = item;
            mItemLabelTextView.setText(item.getLabel());
            mItemCategoryTextView.setText(item.getCategory());
        }

        @Override
        public void onClick(View view) {
        }
    }

    private void setupAdapter() {
        mItemsRecyclerView.setAdapter(new ItemsAdapter(mItems));
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
    }

}
