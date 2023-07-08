package com.hemu.hindinewslivetv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hemu.hindinewslivetv.adopters.ChannelAdopters;
import com.hemu.hindinewslivetv.models.Channel;
import com.hemu.hindinewslivetv.services.ChannelDataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListingActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    RecyclerView newsChannelList;
    ChannelAdopters newsChannelAdopters;
    List<Channel> newsChannels;
    ChannelDataService service;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);

        service = new ChannelDataService(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String activity = extras.getString("activity");
            if (activity.equals("tv_channel")){
                getSupportActionBar().setTitle(R.string.news_bn);
                getListActivity(getString(R.string.json_url),"small_item",2);
            }
            if (activity.equals("news_paper")){
                getSupportActionBar().setTitle(R.string.news_paper_bn);
                getListActivity(getString(R.string.ePaper_url),"small_item",2);
            }
            if (activity.equals("news_publisher")){
                getSupportActionBar().setTitle(R.string.owner_of_news_bn);
                getListActivity(getString(R.string.json_url),"big_item",1);
            }
        }
    }


    public void getListActivity(String url,String item, int spanCount) {
        newsChannelList = findViewById(R.id.recyclerView);
        newsChannels = new ArrayList<>();
        newsChannelList.setLayoutManager(new GridLayoutManager(this, spanCount, LinearLayoutManager.VERTICAL,false));
        newsChannelAdopters = new ChannelAdopters(this, newsChannels, item){
            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                progressBar.setVisibility(View.GONE);
            }
        };
        newsChannelList.setAdapter(newsChannelAdopters);
        service.getChannelData(url, new ChannelDataService.OnDataResponse() {
            @Override
            public Void onError(String error) {
                Log.d(TAG, "onErrorResponse: " + error);
                return null;
            }

            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject channelData =  response.getJSONObject(i);
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
                        newsChannels.add(c);
                        newsChannelAdopters.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}