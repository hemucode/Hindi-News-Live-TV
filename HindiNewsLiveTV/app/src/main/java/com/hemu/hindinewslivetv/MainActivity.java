package com.hemu.hindinewslivetv;

import static com.hemu.hindinewslivetv.LauncherActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hemu.hindinewslivetv.models.Common;
import com.hemu.hindinewslivetv.models.InAppUpdate;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    CardView cardView1,cardView2;
    String appsName, packageName;
    AdView AdView,AdView1;
    InAppUpdate inAppUpdate;



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
                share.putExtra("android.intent.extra.TEXT", MainActivity.this.appsName + getString(R.string.download_click) + APP_Download_URL);
                MainActivity.this.startActivity(Intent.createChooser(share, getString(R.string.share_it)));
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

        inAppUpdate = new InAppUpdate(MainActivity.this);
        inAppUpdate.checkForAppUpdate();

        if (Common.isConnectToInternet(this)) {
            new webScript().execute();
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
    private class webScript extends AsyncTask<Void , Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences getShared = getSharedPreferences("shorts", MODE_PRIVATE);

            String JsonValue = getShared.getString("row","noValue");

            String JsonValueEdit = getShared.getString("edit","noValue");

            if (!JsonValue.equals("noValue") && !JsonValueEdit.equals("noValue") ){
                try {
                    JSONArray jsonArray = new JSONArray(JsonValue);
                    JSONArray jsonArrayEdit = new JSONArray(JsonValueEdit);

                    JSONObject row = jsonArray.getJSONObject(0);
                    JSONObject edit = jsonArrayEdit.getJSONObject(0);

                    if (!row.getString("title").equals(edit.getString("title"))||
                            edit.getString("desc").equals("")){
                        JSONArray arr = new JSONArray();
                        HashMap<String, JSONObject> map = new HashMap<>();

                        Document document;
                        String desc;

                        for (int i = 0; i < 20; i++){
                            JSONObject channelData = jsonArray.getJSONObject(i);
                            document = Jsoup.connect(channelData.getString("link")).get();





                            Elements rightSec = document.select(".khbr_rght_sec").select("p");
                            Elements container = document.select(".container").select("p");
                            Elements slider_con = document.select(".slider_con").select("p");
                            Elements all_p = document.select("p");
                            if (rightSec.first()!= null){
                                if (rightSec.text().length() > 400){
                                    desc = rightSec.text().substring(0,400);
                                }else {
                                    desc = rightSec.text();
                                }
                            }else if (container.first()!=null){
                                if (container.text().length() > 400){
                                    desc = container.text().substring(0,400);
                                }else {
                                    desc = container.text();
                                }
                            }else if (slider_con.first()!=null){
                                if (slider_con.text().length() > 401){
                                    desc = slider_con.text().substring(0,400);
                                }else {
                                    desc = slider_con.text();
                                }
                            }else if (all_p.first()!=null){
                                if (all_p.text().length() > 400){
                                    desc = all_p.text().substring(0,400);
                                }else {
                                    desc = all_p.text();
                                }
                            }else {
                                desc = ".....";
                            }
                            JSONObject json = new JSONObject();


                            json.put("id",i);
                            json.put("title",channelData.getString("title"));
                            json.put("desc",desc);
                            json.put("link",channelData.getString("link"));
                            json.put("thumbnail" ,channelData.getString("thumbnail"));
                            map.put("json" + i, json);
                            arr.put(map.get("json" + i));
                            Log.d(TAG, "1onErrorResponse: " + desc);
                            Log.d(TAG, "1onErrorResponse: " + channelData.getString("thumbnail"));
                        }
                        SharedPreferences sharedPreferences = getSharedPreferences("shorts", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("edit",arr.toString());
                        editor.apply();
                    }

                } catch (JSONException e) {
                    //throw new RuntimeException(e);
                } catch (IOException e) {
                    // throw new RuntimeException(e);
                }
            }

            if (!JsonValue.equals("noValue") && JsonValueEdit.equals("noValue")){
                try {
                    JSONArray arr = new JSONArray();
                    HashMap<String, JSONObject> map = new HashMap<>();

                    JSONArray jsonArray = new JSONArray(JsonValue);
                    Document document;
                    String desc;

                    for (int i = 0; i < 20; i++){
                        JSONObject channelData = jsonArray.getJSONObject(i);

                        document = Jsoup.connect(channelData.getString("link")).get();

                        Elements rightSec = document.select(".khbr_rght_sec").select("p");
                        Elements container = document.select(".container").select("p");
                        Elements slider_con = document.select(".slider_con").select("p");
                        Elements all_p = document.select("p");
                        if (rightSec.first()!= null){
                            if (rightSec.text().length() > 400){
                                desc = rightSec.text().substring(0,400);
                            }else {
                                desc = rightSec.text();
                            }
                        }else if (container.first()!=null){
                            if (container.text().length() > 400){
                                desc = container.text().substring(0,400);
                            }else {
                                desc = container.text();
                            }
                        }else if (slider_con.first()!=null){
                            if (slider_con.text().length() > 401){
                                desc = slider_con.text().substring(0,400);
                            }else {
                                desc = slider_con.text();
                            }
                        }else if (all_p.first()!=null){
                            if (all_p.text().length() > 400){
                                desc = all_p.text().substring(0,400);
                            }else {
                                desc = all_p.text();
                            }
                        }else {
                            desc = "........";
                        }


                        JSONObject json = new JSONObject();

                        json.put("id",i);
                        json.put("title",channelData.getString("title"));
                        json.put("desc",desc);
                        json.put("link",channelData.getString("link"));
                        json.put("thumbnail",channelData.getString("thumbnail"));
                        map.put("json" + i, json);
                        arr.put(map.get("json" + i));
                        Log.d(TAG, "1onErrorResponse: " + desc);
                        Log.d(TAG, "1onErrorResponse: " + channelData.getString("thumbnail"));
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("shorts", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("edit",arr.toString());
                    //Log.d(TAG, "1onsResponse: " + arr);
                    editor.apply();
                } catch (JSONException | IOException e) {
                    // Log.d(TAG, "1onErrorResponse: " + e);
                }

            }

            return null;
        }


    }

}