package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {


    TwitterClient client;
    JsonHttpResponseHandler handler;
    Tweet tweet;
    Button button;
    EditText addTweet;
    String newTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);
        addTweet = (EditText) findViewById(R.id.etTweet);
        button = (Button) findViewById(R.id.addTweet);
//        onCompose(findViewById(android.R.id.content));
        changeColor();

    }

    public void onCompose(View view) {

        newTweet = addTweet.getText().toString();
        Toast.makeText(this, "Hello the button was clicked", Toast.LENGTH_LONG).show();
        client.sendTweet(newTweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Tweet tweet = null;

                try {
                    tweet = Tweet.fromJSON(response);
                    // Do something in response to button click
                    Intent intent = new Intent();
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    public void changeColor() {
        TextInputLayout layout = (TextInputLayout) findViewById(R.id.textInput);
        int diff = (layout.getCounterMaxLength() - layout.getChildCount());
        if (diff < 0) {
            addTweet.setTextColor(Color.parseColor("#990000"));
        }
    }


//    public void onSuccess(int statusCode, Header[] headers, JSONObject response) throws JSONException {
//        Tweet tweet = new Tweet();
//        tweet.fromJSON(response);
//        Intent intent = new Intent(this, TimelineActivity.class);
//        intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
//        this.startActivity(intent);
//    }

}
