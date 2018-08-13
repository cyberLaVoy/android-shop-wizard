package com.example.cyberlavoy.shopwizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.Callable;

public class SplashActivity extends AppCompatActivity {

    private static String apiUrl = "https://stormy-everglades-69504.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final ResponseParser responseParser = new ResponseParser(getApplicationContext());
        final String[] onItemsRequestResponseArray = new String[1];
        RequestHandler.getInstance(getApplicationContext()).handleGETRequest(apiUrl + "/ingredients", onItemsRequestResponseArray, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                responseParser.parseItemsJSONString(onItemsRequestResponseArray[0]);
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
