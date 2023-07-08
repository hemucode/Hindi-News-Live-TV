package com.hemu.hindinewslivetv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.monstertechno.adblocker.AdBlockerWebView;

public class WebActivity extends AppCompatActivity {
    WebView web;
    ProgressBar progressBar;
    private AdView adView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean loadingFinished = true;
    boolean redirect = false;
    LinearLayout linearLayout;
    Button button1,button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar3);
        linearLayout = findViewById(R.id.sorryLayout);

        button1 = findViewById(R.id.button3);

        button2 = findViewById(R.id.button4);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        new AdBlockerWebView.init(this).initializeWebView(web);
        if (extras != null) {
            String title = extras.getString("title");
            String url = extras.getString("url");

            getSupportActionBar().setTitle(title);
            web.loadUrl(url);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent("android.intent.action.VIEW");
                    intent2.setData(Uri.parse(url));
                    WebActivity.this.startActivity(intent2);
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setLoadWithOverviewMode(true);
            web.getSettings().setUseWideViewPort(true);

            web.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    web.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                    if (!loadingFinished) {
                        redirect = true;
                    }

                    loadingFinished = false;
                    view.loadUrl(urlNewString);
                    return true;
                }
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    loadingFinished = false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (!redirect) {
                        loadingFinished = true;
                    }
                    if (loadingFinished && !redirect) {
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        redirect = false;
                    }
                }
            });

        }

        loadFacebookAds();

        mSwipeRefreshLayout = findViewById(R.id.refresh_app);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                web.loadUrl("javascript:window.location.reload( true )");
                loadFacebookAds();
            }
        });

    }


    public void loadFacebookAds() {
        if (adView != null) {
            adView.destroy();
        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}