package com.hemu.hindinewslivetv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Calendar;

public class SecondActivity extends AppCompatActivity {
    TextView textView;
    CardView CardView1,CardView2,CardView3,CardView4;
    private AdView AdView,AdView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSupportActionBar().hide();

        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        textView = findViewById(R.id.textView);
        textView.setText(String.format(getString(R.string.news_bn)) +" "+ year);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView = findViewById(R.id.adView);
        AdView1 = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView.loadAd(adRequest);
        AdView1.loadAd(adRequest);

        CardView1 = findViewById(R.id.CardView1);
        CardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cp = new Intent(SecondActivity.this,ListingActivity.class);
                cp.putExtra("activity","tv_channel");
                startActivity(cp);
            }
        });
        CardView2 = findViewById(R.id.CardView2);
        CardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cp = new Intent(SecondActivity.this,ListingActivity.class);
                cp.putExtra("activity","news_paper");
                startActivity(cp);
            }
        });
        CardView3 = findViewById(R.id.CardView3);
        CardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cp = new Intent(SecondActivity.this,WebActivity.class);
                cp.putExtra("activity","news_paper");
                cp.putExtra("title",getString(R.string.privacy_policy));
                cp.putExtra("url",getString(R.string.privacy_policy_url));
                startActivity(cp);
            }
        });
        CardView4 = findViewById(R.id.CardView4);
        CardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cp = new Intent(SecondActivity.this,ListingActivity.class);
                cp.putExtra("activity","news_publisher");
                startActivity(cp);
            }
        });

    }

}