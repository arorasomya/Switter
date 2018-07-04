package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends AppCompatActivity {


    TwitterClient client;
    JsonHttpResponseHandler handler;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        EditText addTweet = (EditText) findViewById(R.id.etTweet);
        String newTweet = addTweet.getText().toString();
        handler = new JsonHttpResponseHandler();
        client = new TwitterClient(this);
        client.sendTweet(newTweet, handler);
    }
    



}
