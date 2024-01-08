package com.hemu.hindinewslivetv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.hemu.hindinewslivetv.models.Common;
import com.hemu.hindinewslivetv.services.ShortDataAsync;
import com.hemu.hindinewslivetv.services.ShortDesAsync;


import java.util.Calendar;
import java.util.Objects;


public class SecondActivity extends AppCompatActivity {
    CardView CardView1,CardView2,CardView3,CardView4,CardView5,CardView6;
    AdView AdView1;
    ReviewManager manager;
    ReviewInfo reviewInfo;


    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

        getSupportActionBar().setTitle("हिंदी समाचार "+ year);



        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView1 = findViewById(R.id.adView1);
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
        CardView5.setOnClickListener(v -> startActivity(new Intent(SecondActivity.this,ShortActivity.class)));
        CardView6 = findViewById(R.id.CardView33);
        CardView6.setOnClickListener(v -> startActivity(new Intent(SecondActivity.this,WebActivity.class)
                .putExtra("title",getString(R.string.election_ml))
                .putExtra("url",getString(R.string.election_ml_link))));

        RequestReviewInfo();


        SharedPreferences sharedPreferences = getSharedPreferences("app_open", MODE_PRIVATE);
        String CountNumber = sharedPreferences.getString("count","noValue");
        if (CountNumber.equals("10")){RateMe();}

    }
    private void RateMe(){
        if (reviewInfo != null){
            Task<Void> flow = manager.launchReviewFlow(this,reviewInfo);

            flow.addOnCompleteListener(task -> {
            });
        }

    }

    private void RequestReviewInfo(){
        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();

        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                reviewInfo = task.getResult();
            }else {
                Toast.makeText(this, "Not Review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.rate) {
            RateMe();
        }
        if (item.getItemId() == R.id.settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
            View view = getLayoutInflater().inflate(R.layout.dialog_rss,null);
            builder.setIcon(R.drawable.shorts);
            builder.setTitle(R.string.short_categories);
            Spinner spinner = view.findViewById(R.id.spinner);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SecondActivity.this, android.R.layout.simple_spinner_item,SecondActivity.this.getResources().getStringArray(R.array.rssList));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            Button button = view.findViewById(R.id.save);
            Button button1 = view.findViewById(R.id.back);
            SharedPreferences sharedPreferences = getSharedPreferences("BigBengaliJson", Context.MODE_PRIVATE);
            String rssPosition = sharedPreferences.getString("rss","noValue");
            if (!rssPosition.equals("noValue")){
                spinner.setSelection(Integer.parseInt(rssPosition));
            }
            builder.setView(view);
            AlertDialog mDialog =  builder.create();
            mDialog.show();

            button.setOnClickListener(v1 -> {
                if (spinner.getSelectedItemPosition()!=0){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("rss",String.valueOf(spinner.getSelectedItemPosition()));
                    editor.apply();
                    Toast.makeText(SecondActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    if (Common.isConnectToInternet(SecondActivity.this)) {
                        new ShortDataAsync(SecondActivity.this).execute();
                        try {
                            Thread.sleep(2000);
                            new ShortDesAsync(SecondActivity.this).execute();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    mDialog.dismiss();
                }

            });

            button1.setOnClickListener(v12 -> mDialog.dismiss());


        }
        return super.onOptionsItemSelected(item);
    }
}