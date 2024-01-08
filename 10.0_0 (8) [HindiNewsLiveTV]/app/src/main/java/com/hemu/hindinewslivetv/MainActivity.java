package com.hemu.hindinewslivetv;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.content.ActivityNotFoundException;

import android.content.Intent;


import android.net.Uri;

import android.os.Bundle;


import android.widget.Button;

import android.widget.TextView;

import com.hemu.hindinewslivetv.models.Common;
import com.hemu.hindinewslivetv.models.InAppUpdate;
import com.hemu.hindinewslivetv.services.ShortDesAsync;

import org.jetbrains.annotations.Nullable;



import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    CardView cardView1,cardView2;
    String appsName, packageName;

    InAppUpdate inAppUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();


        appsName = getApplication().getString(R.string.app_name);
        packageName = getApplication().getPackageName();

        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        textView = findViewById(R.id.textView);
        textView.setText(String.format("%s %s", String.format(getString(R.string.short_name)), year));




        button = findViewById(R.id.button);
        button.setOnClickListener(v -> MainActivity.this.startActivity(new Intent(MainActivity.this, SecondActivity.class)));

        cardView1 = findViewById(R.id.CardView1);
        this.cardView1.setOnClickListener(view -> {
            Intent share = new Intent("android.intent.action.SEND");
            share.setType("text/plain");
            share.putExtra("android.intent.extra.SUBJECT", MainActivity.this.appsName);
            String APP_Download_URL = "https://play.google.com/store/apps/details?id=" + MainActivity.this.packageName;
            share.putExtra("android.intent.extra.TEXT", MainActivity.this.appsName + getString(R.string.download_click) + APP_Download_URL);
            MainActivity.this.startActivity(Intent.createChooser(share, getString(R.string.share_it)));
        });

        cardView2 = findViewById(R.id.CardView2);
        cardView2.setOnClickListener(view -> {
            try {
                Intent intent2 = new Intent("android.intent.action.VIEW");
                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + MainActivity.this.packageName));
                MainActivity.this.startActivity(intent2);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("market://details?id=" + MainActivity.this.packageName));
                MainActivity.this.startActivity(intent);
            }
        });


        inAppUpdate = new InAppUpdate(MainActivity.this);
        inAppUpdate.checkForAppUpdate();

        if (Common.isConnectToInternet(MainActivity.this)) {
            new ShortDesAsync(MainActivity.this).execute();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inAppUpdate.onActivityResult(requestCode, resultCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        inAppUpdate.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inAppUpdate.onDestroy();
    }


}