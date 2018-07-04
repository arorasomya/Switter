package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.ComposeActivity;
import com.codepath.apps.restclienttemplate.models.SampleModel;
import com.codepath.apps.restclienttemplate.models.SampleModelDao;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    public static final int COMPOSE_REQ = 1;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient(this);
        // find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        // instantiate the arraylist (data source)
        tweets = new ArrayList<>();

        // construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);
        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);
        populateTimeline();

    }
    public void onComposeAction(MenuItem menuItem) {
        // first parameter is the context, and second is the activity to go to
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, COMPOSE_REQ); // brings up the second activity
    }
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Log.d("TwitterClient", response.toString());
                // iterate through the JSON array
                // for each entry, deserialize the JSON object
                for (int i = 0; i < response.length(); i++) {
                    // convert each object to a tweet model

                    // add that Tweet model to out data source
                    // notify the adapter that we've added an item

                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    public static class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            final SampleModel sampleModel = new SampleModel();
            sampleModel.setName("CodePath");

            final SampleModelDao sampleModelDao = ((TwitterApp) getApplicationContext()).getMyDatabase().sampleModelDao();

            AsyncTask<SampleModel, Void, Void> task = new AsyncTask<SampleModel, Void, Void>() {
                @Override
                protected Void doInBackground(SampleModel... sampleModels) {
                    sampleModelDao.insertModel(sampleModels);
                    return null;
                };
            };
            task.execute(sampleModel);
        }


        // Inflate the menu; this adds items to the action bar if it is present.
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.login, menu);
            return true;
        }

        // OAuth authenticated successfully, launch primary authenticated activity
        // i.e Display application "homepage"
        @Override
        public void onLoginSuccess() {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, TimelineActivity.class);
            startActivity(i);
        }

        // OAuth authentication flow failed, handle the error
        // i.e Display an error dialog or toast
        @Override
        public void onLoginFailure(Exception e) {
            e.printStackTrace();
        }

        // Click handler method for the button used to start OAuth flow
        // Uses the client to initiate OAuth authorization
        // This should be tied to a button used to login
        public void loginToRest(View view) {
            getClient().connect();
        }

    }
}
