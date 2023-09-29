package com.hemu.hindinewslivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.cardview.widget.CardView;

import android.content.Intent;


import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


import java.util.Calendar;
import java.util.Objects;


public class SecondActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    TextView textView;
    CardView CardView1,CardView2,CardView3,CardView4,CardView5,CardView6;
    AdView AdView,AdView1;


    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Objects.requireNonNull(getSupportActionBar()).hide();


        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        textView = findViewById(R.id.textView);
        textView.setText(String.format(getString(R.string.news_bn)) +" "+ year);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView = findViewById(R.id.adView);
        AdView1 = findViewById(R.id.adView1);
        AdView.loadAd(adRequest);
        AdView1.loadAd(adRequest);

        CardView1 = findViewById(R.id.CardView1);
        CardView1.setOnClickListener(v -> {
            Intent cp = new Intent(SecondActivity.this,ListingActivity.class);
            cp.putExtra("activity","tv_channel");
            startActivity(cp);
        });
        CardView2 = findViewById(R.id.CardView2);
        CardView2.setOnClickListener(v -> {
            Intent cp = new Intent(SecondActivity.this,ListingActivity.class);
            cp.putExtra("activity","news_paper");
            startActivity(cp);
        });
        CardView3 = findViewById(R.id.CardView3);
        CardView3.setOnClickListener(v -> {
            Intent cp = new Intent(SecondActivity.this,WebActivity.class);
            cp.putExtra("title",getString(R.string.privacy_policy));
            cp.putExtra("url",getString(R.string.privacy_policy_url));
            startActivity(cp);
        });
        CardView4 = findViewById(R.id.CardView4);
        CardView4.setOnClickListener(v -> {
            Intent cp = new Intent(SecondActivity.this,ListingActivity.class);
            cp.putExtra("activity","news_publisher");
            startActivity(cp);
        });
        CardView5 = findViewById(R.id.CardView32);
        CardView5.setOnClickListener(v -> {
            startActivity(new Intent(SecondActivity.this,ShortActivity.class));
        });
        CardView6 = findViewById(R.id.CardView33);
        CardView6.setOnClickListener(v -> {
            startActivity(new Intent(SecondActivity.this,WebActivity.class)
                    .putExtra("title",getString(R.string.election_ml))
                    .putExtra("url",getString(R.string.election_ml_link)));
        });


    }


}