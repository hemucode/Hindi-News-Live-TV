package com.hemu.hindinewslivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdView;
import com.hemu.hindinewslivetv.adopters.ChannelAdopters;
import com.hemu.hindinewslivetv.models.Channel;
import com.hemu.hindinewslivetv.models.Common;
import com.hemu.hindinewslivetv.services.ChannelDataService;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ListingActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    RecyclerView newsChannelList;
    ChannelAdopters newsChannelAdopters1,newsChannelAdopters2,newsChannelAdopters3;
    List<Channel> newsChannels1,newsChannels2,newsChannels3;
    ChannelDataService service;
    ProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        service = new ChannelDataService(this);



        progressBar = findViewById(R.id.progressBar);

        mSwipeRefreshLayout = findViewById(R.id.swipe);

        newsChannelList = findViewById(R.id.recyclerView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String activity = extras.getString("activity");
            if (activity.equals("tv_channel")) {
                getSupportActionBar().setTitle(R.string.news_bn);
                getListActivity1("no", getString(R.string.news_url));
                mSwipeRefreshLayout.setOnRefreshListener(() -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    getListActivity1("yes", getString(R.string.news_url));

                });
            }
            if (activity.equals("news_paper")) {
                getSupportActionBar().setTitle(R.string.news_paper_bn);
                getListActivity2("no", getString(R.string.ePaper_url));
                mSwipeRefreshLayout.setOnRefreshListener(() -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    getListActivity2("yes", getString(R.string.news_url));

                });

            }
            if (activity.equals("news_publisher")) {
                getListActivity3("no", getString(R.string.news_url));
                mSwipeRefreshLayout.setOnRefreshListener(() -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    getListActivity3("yes", getString(R.string.news_url));

                });

            }
        }
    }
    public void getListActivity1(String refresh,String url) {
        newsChannelList.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        newsChannels1 = new ArrayList<>();
        newsChannelAdopters1 = new ChannelAdopters(this, newsChannels1, "small_item"){
            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                progressBar.setVisibility(View.GONE);
                super.onBindViewHolder(holder, position);
            }

        };
        newsChannelList.setAdapter(newsChannelAdopters1);


        SharedPreferences getShared = getSharedPreferences("hindiJson", MODE_PRIVATE);
        String JsonValue = getShared.getString("str","noValue");


        if (refresh.equals("yes")){
            service.getChannelData( url, new ChannelDataService.OnDataResponse() {
                @Override
                public Void onError(String error) {
                    Log.d(TAG, "1onErrorResponse: " + error);
                    return null;
                }
                @Override
                public void onResponse(JSONArray response) {
                    SharedPreferences sharedPreferences = getSharedPreferences("hindiJson",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("str",response.toString());
                    editor.apply();
                }
            });
        }

        if (JsonValue.equals("noValue")) {
            service.getChannelData( url, new ChannelDataService.OnDataResponse() {
                @Override
                public Void onError(String error) {
                    Log.d(TAG, "onErrorResponse: " + error);
                    return null;
                }
                @Override
                public void onResponse(JSONArray response) {
                    SharedPreferences sharedPreferences = getSharedPreferences("hindiJson",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("str",response.toString());
                    editor.apply();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject channelData = response.getJSONObject(i);
                            Channel c = new Channel();
                            c.setId(channelData.getInt("id"));
                            c.setName(channelData.getString("name"));
                            c.setDescription(channelData.getString("description"));
                            c.setLive_url(channelData.getString("live_url"));
                            c.setThumbnail(channelData.getString("thumbnail"));
                            c.setFacebook(channelData.getString("facebook"));
                            c.setYoutube(channelData.getString("youtube"));
                            c.setWebsite(channelData.getString("website"));
                            c.setCategory(channelData.getString("category"));
                            c.setLiveTvLink(channelData.getString("liveTvLink"));
                            c.setContact(channelData.getString("contact"));
                            newsChannels1.add(c);
                            newsChannelAdopters1.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
        }else {
            try {
                JSONArray jsonArray = new JSONArray(JsonValue);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject channelData = jsonArray.getJSONObject(i);
                        Channel c = new Channel();
                        c.setId(channelData.getInt("id"));
                        c.setName(channelData.getString("name"));
                        c.setDescription(channelData.getString("description"));
                        c.setLive_url(channelData.getString("live_url"));
                        c.setThumbnail(channelData.getString("thumbnail"));
                        c.setFacebook(channelData.getString("facebook"));
                        c.setYoutube(channelData.getString("youtube"));
                        c.setWebsite(channelData.getString("website"));
                        c.setCategory(channelData.getString("category"));
                        c.setLiveTvLink(channelData.getString("liveTvLink"));
                        c.setContact(channelData.getString("contact"));
                        newsChannels1.add(c);
                        newsChannelAdopters1.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void getListActivity2(String refresh,String url) {
        newsChannelList.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));

        newsChannels2 = new ArrayList<>();
        newsChannelAdopters2 = new ChannelAdopters(this, newsChannels2, "paper_item"){
            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                progressBar.setVisibility(View.GONE);
                super.onBindViewHolder(holder, position);
            }

        };
        newsChannelList.setAdapter(newsChannelAdopters2);


        SharedPreferences getShared = getSharedPreferences("paperJson", MODE_PRIVATE);
        String JsonValue = getShared.getString("str","noValue");


        if (refresh.equals("yes")){
            service.getChannelData( url, new ChannelDataService.OnDataResponse() {
                @Override
                public Void onError(String error) {
                    Log.d(TAG, "onErrorResponse: " + error);
                    return null;
                }
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, "onErrorResponse: " + response.toString());
                    SharedPreferences sharedPreferences = getSharedPreferences("paperJson",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("str",response.toString());
                    editor.apply();
                }
            });
        }

        if (JsonValue.equals("noValue")) {
            service.getChannelData( url, new ChannelDataService.OnDataResponse() {
                @Override
                public Void onError(String error) {
                    Log.d(TAG, "onErrorResponse: " + error);
                    return null;
                }
                @Override
                public void onResponse(JSONArray response) {
                    SharedPreferences sharedPreferences = getSharedPreferences("paperJson",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("str",response.toString());
                    editor.apply();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject channelData = response.getJSONObject(i);
                            Channel c = new Channel();
                            c.setId(channelData.getInt("id"));
                            c.setName(channelData.getString("name"));
                            c.setDescription(channelData.getString("description"));
                            c.setLive_url(channelData.getString("live_url"));
                            c.setThumbnail(channelData.getString("thumbnail"));
                            c.setFacebook(channelData.getString("facebook"));
                            c.setYoutube(channelData.getString("youtube"));
                            c.setWebsite(channelData.getString("website"));
                            c.setCategory(channelData.getString("category"));
                            c.setLiveTvLink(channelData.getString("liveTvLink"));
                            c.setContact(channelData.getString("contact"));
                            newsChannels2.add(c);
                            newsChannelAdopters2.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
        }else {
            try {
                JSONArray jsonArray = new JSONArray(JsonValue);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject channelData = jsonArray.getJSONObject(i);
                        Channel c = new Channel();
                        c.setId(channelData.getInt("id"));
                        c.setName(channelData.getString("name"));
                        c.setDescription(channelData.getString("description"));
                        c.setLive_url(channelData.getString("live_url"));
                        c.setThumbnail(channelData.getString("thumbnail"));
                        c.setFacebook(channelData.getString("facebook"));
                        c.setYoutube(channelData.getString("youtube"));
                        c.setWebsite(channelData.getString("website"));
                        c.setCategory(channelData.getString("category"));
                        c.setLiveTvLink(channelData.getString("liveTvLink"));
                        c.setContact(channelData.getString("contact"));
                        newsChannels2.add(c);
                        newsChannelAdopters2.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void getListActivity3(String refresh,String url) {
        newsChannelList = findViewById(R.id.recyclerView);
        newsChannelList.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));

        newsChannels3 = new ArrayList<>();
        newsChannelAdopters3 = new ChannelAdopters(this, newsChannels3, "item"){
            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                progressBar.setVisibility(View.GONE);
                super.onBindViewHolder(holder, position);
            }

        };
        newsChannelList.setAdapter(newsChannelAdopters3);


        SharedPreferences getShared = getSharedPreferences("hindiJson", MODE_PRIVATE);
        String JsonValue = getShared.getString("str","noValue");


        if (refresh.equals("yes")){
            service.getChannelData( url, new ChannelDataService.OnDataResponse() {
                @Override
                public Void onError(String error) {
                    Log.d(TAG, "onErrorResponse: " + error);
                    return null;
                }
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, "onErrorResponse: " + response.toString());
                    SharedPreferences sharedPreferences = getSharedPreferences("hindiJson",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("str",response.toString());
                    editor.apply();
                }
            });
        }

        if (JsonValue.equals("noValue")) {
            service.getChannelData( url, new ChannelDataService.OnDataResponse() {
                @Override
                public Void onError(String error) {
                    Log.d(TAG, "onErrorResponse: " + error);
                    return null;
                }
                @Override
                public void onResponse(JSONArray response) {
                    SharedPreferences sharedPreferences = getSharedPreferences("hindiJson",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("str",response.toString());
                    editor.apply();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject channelData = response.getJSONObject(i);
                            Channel c = new Channel();
                            c.setId(channelData.getInt("id"));
                            c.setName(channelData.getString("name"));
                            c.setDescription(channelData.getString("description"));
                            c.setLive_url(channelData.getString("live_url"));
                            c.setThumbnail(channelData.getString("thumbnail"));
                            c.setFacebook(channelData.getString("facebook"));
                            c.setYoutube(channelData.getString("youtube"));
                            c.setWebsite(channelData.getString("website"));
                            c.setCategory(channelData.getString("category"));
                            c.setLiveTvLink(channelData.getString("liveTvLink"));
                            c.setContact(channelData.getString("contact"));
                            newsChannels3.add(c);
                            newsChannelAdopters3.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
        }else {
            try {
                JSONArray jsonArray = new JSONArray(JsonValue);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject channelData = jsonArray.getJSONObject(i);
                        Channel c = new Channel();
                        c.setId(channelData.getInt("id"));
                        c.setName(channelData.getString("name"));
                        c.setDescription(channelData.getString("description"));
                        c.setLive_url(channelData.getString("live_url"));
                        c.setThumbnail(channelData.getString("thumbnail"));
                        c.setFacebook(channelData.getString("facebook"));
                        c.setYoutube(channelData.getString("youtube"));
                        c.setWebsite(channelData.getString("website"));
                        c.setCategory(channelData.getString("category"));
                        c.setLiveTvLink(channelData.getString("liveTvLink"));
                        c.setContact(channelData.getString("contact"));
                        newsChannels3.add(c);
                        newsChannelAdopters3.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}