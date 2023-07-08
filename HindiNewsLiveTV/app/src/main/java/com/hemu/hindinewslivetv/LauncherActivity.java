package com.hemu.hindinewslivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LauncherActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().hide();

        linearLayout =findViewById(R.id.linearLayout);
        imageView = findViewById(R.id.imageView);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        },500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LauncherActivity.this.startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                finish();
            }
        },2000);
    }
}