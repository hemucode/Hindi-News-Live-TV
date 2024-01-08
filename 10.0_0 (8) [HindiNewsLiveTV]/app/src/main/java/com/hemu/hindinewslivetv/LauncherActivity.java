package com.hemu.hindinewslivetv;


import android.annotation.SuppressLint;

import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.Handler;

import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hemu.hindinewslivetv.models.Common;
import com.hemu.hindinewslivetv.services.ShortDataAsync;

import java.util.Objects;

public class LauncherActivity extends AppCompatActivity {
    TextView textView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();


        textView = findViewById(R.id.textView);
        PackageManager manager = this.getPackageManager();
        textView = findViewById(R.id.textView);
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            String versionName = info.versionName;

            textView.setText("Version " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("app_open", MODE_PRIVATE);

        String CountNumber = sharedPreferences.getString("count","noValue");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (CountNumber.equals("noValue")){
            editor.putString("count","1");

        }else {
            int Counting = Integer.parseInt(CountNumber) + 1;
            editor.putString("count",String.valueOf(Counting));
        }
        editor.apply();





        Handler handler = new Handler();

        handler.postDelayed(() -> {
            LauncherActivity.this.startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            finish();
        },1500);

        if (Common.isConnectToInternet(LauncherActivity.this)) {
            new ShortDataAsync(LauncherActivity.this).execute();
        }
    }

}