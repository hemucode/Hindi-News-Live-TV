package com.hemu.hindinewslivetv;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hemu.hindinewslivetv.models.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class LauncherActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        String versionName = BuildConfig.VERSION_NAME;
        textView = findViewById(R.id.textView);
        textView.setText("Version "+versionName);


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LauncherActivity.this.startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                finish();
            }
        },1500);

        if (Common.isConnectToInternet(LauncherActivity.this)) {
            new webScript().execute();
        }
    }
    private class webScript extends AsyncTask<Void , Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Document document  = null;
            Elements titleE,linksE,thumbnailE;
            String title,links,thumbnail;

            try {
                document = Jsoup.connect(getString(R.string.rss_feed )).get();

                JSONArray arr = new JSONArray();
                HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();

                for (int i =0; i< 20; i++){
                    titleE = document.select("item").select("title");
                    linksE = document.select("item").select("link");
                    thumbnailE = document.select("item").select("media|content");

                    if (!titleE.isEmpty() && !linksE.isEmpty()) {
                        Element titles = titleE.get(i);
                        Element link = linksE.get(i);
                        Element thumbnailEl = thumbnailE.get(i);


                        if (titles!=null && link!=null && thumbnailEl!=null){
                            title = titles.text();
                            links = link.text();
                            thumbnail = thumbnailEl.attr("url");
                            JSONObject json = new JSONObject();
                            json.put("id",i);
                            json.put("title",title);
                            json.put("link",links);
                            json.put("thumbnail",thumbnail);
                            map.put("json" + i, json);
                            arr.put(map.get("json" + i));
                        }
                    }

                }

                SharedPreferences sharedPreferences = getSharedPreferences("shorts", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("row",arr.toString());
                Log.d(TAG, "1onResponse: " + arr);
                editor.apply();


            } catch (IOException e) {
                Log.d(TAG, "1onError: RSS Feed Url Connect Error =" + e);
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            } catch (JSONException e) {
                Log.d(TAG, "1onError: RSS Feed Json Load Error =" + e);
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            }
            return null;
        }

    }
}