package com.example.cyberlavoy.shopwizard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.Callable;

public class SplashActivity extends AppCompatActivity {

    private String apiUrl = "https://stormy-everglades-69504.herokuapp.com";
    private Context mContext;
    private ResponseParser mResponseParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mResponseParser = new ResponseParser(mContext);
        ListStore.getInstance(getApplicationContext()).resetStore();
        requestShoppingLists();
        requestRecipes();
        requestItems(); // app will start on response from this request
    }
    public void requestShoppingLists() {
        final String[] onShoppingListsRequestResponseArray = new String[1];
        RequestHandler.getInstance(mContext).handleGETRequest(apiUrl + "/groceries", onShoppingListsRequestResponseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                mResponseParser.parseShoppingListsJSONString(onShoppingListsRequestResponseArray[0]);
                return null;
            }
        });
    }
    public void requestRecipes() {
        final String[] onRecipesRequestResponseArray = new String[1];
        RequestHandler.getInstance(mContext).handleGETRequest(apiUrl + "/recipes", onRecipesRequestResponseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                mResponseParser.parseRecipesJSONString(onRecipesRequestResponseArray[0]);
                return null;
            }
        });
    }
    public void requestItems() {
        final String[] onItemsRequestResponseArray = new String[1];
        RequestHandler.getInstance(mContext).handleGETRequest(apiUrl + "/ingredients", onItemsRequestResponseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                mResponseParser.parseItemsJSONString(onItemsRequestResponseArray[0]);
                onResponseAppStart();
                return null;
            }
        });
    }
    public void onResponseAppStart() {
       Intent intent  = new Intent(SplashActivity.this, HomeActivity.class);
       startActivity(intent);
       finish();
    }
}
