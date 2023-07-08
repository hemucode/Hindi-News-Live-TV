package com.hemu.hindinewslivetv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    CardView cardView1,cardView2;
    String appsName, packageName;
    private AdView AdView,AdView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        appsName = getApplication().getString(R.string.app_name);
        packageName = getApplication().getPackageName();

        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        textView = findViewById(R.id.textView);
        textView.setText(String.format(getString(R.string.short_name)) +" "+ year);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        cardView1 = findViewById(R.id.CardView1);
        this.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent("android.intent.action.SEND");
                share.setType("text/plain");
                share.putExtra("android.intent.extra.SUBJECT", MainActivity.this.appsName);
                String APP_Download_URL = "https://play.google.com/store/apps/details?id=" + MainActivity.this.packageName;
                share.putExtra("android.intent.extra.TEXT", MainActivity.this.appsName + " - এপ্সটি ডাউনলোড করতে নিচের লিংকে যান\n\n" + APP_Download_URL);
                MainActivity.this.startActivity(Intent.createChooser(share, "শেয়ার করুন"));
            }
        });

        cardView2 = findViewById(R.id.CardView2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent2 = new Intent("android.intent.action.VIEW");
                    intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + MainActivity.this.packageName));
                    MainActivity.this.startActivity(intent2);
                } catch (ActivityNotFoundException e) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=" + MainActivity.this.packageName));
                    MainActivity.this.startActivity(intent);
                }
            }
        });

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

    }
}