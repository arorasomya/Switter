package com.codepath.apps.restclienttemplate;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    Tweet tweet;
    TextView tvName;
    TextView tvHandle;
    TextView tvDate;
    ImageView ivProfile;
    TextView tvTweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvTweet = (TextView) findViewById(R.id.tvTweet);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        // unwrap the tweet sent in via the intent
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        tvName.setText(tweet.user.name);
        tvDate.setText(tweet.date);
        tvHandle.setText(tweet.tvHandle);
        tvTweet.setText(tweet.body);
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfile);
    }
}
