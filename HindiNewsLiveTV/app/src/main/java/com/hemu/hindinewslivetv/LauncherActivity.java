package com.hemu.hindinewslivetv;

import android.annotation.SuppressLint;

import android.content.Intent;

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
    Handler handler;
    TextView textView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        textView = findViewById(R.id.textView);
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            String versionName = info.versionName;
            textView.setText("Version " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        handler = new Handler();
        handler.postDelayed(() -> {
            LauncherActivity.this.startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            finish();
        },1500);

        if (Common.isConnectToInternet(LauncherActivity.this)) {
            new ShortDataAsync(LauncherActivity.this).execute();
        }
    }


}