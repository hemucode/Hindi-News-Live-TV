package com.hemu.hindinewslivetv;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hemu.hindinewslivetv.adopters.ShortsAdopters;

import com.hemu.hindinewslivetv.models.VerticalViewPager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import java.util.Objects;

public class ShortActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    ShortsAdopters shortsAdopters;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> image = new ArrayList<>();
    ArrayList<String> link = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short);
        Objects.requireNonNull(getSupportActionBar()).hide();

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(ShortActivity.this,getString(R.string.InterstitialAd), adRequest,
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    mInterstitialAd = interstitialAd;
                }
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    mInterstitialAd = null;
                }
        });

        SharedPreferences getShared = getSharedPreferences("shorts", MODE_PRIVATE);
        String JsonValue = getShared.getString("edit","noValue");

        if (!JsonValue.equals("noValue")){
            final VerticalViewPager verticalViewPages = findViewById(R.id.VerticalViewPage);

            shortsAdopters = new ShortsAdopters(ShortActivity.this,title,desc,image,link){
                @NonNull
                @Override
                public Object instantiateItem(@NonNull ViewGroup container, int position) {
                    if (position==5){
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(ShortActivity.this);
                        }
                    }
                    return super.instantiateItem(container, position);
                }
            };
            verticalViewPages.setAdapter(shortsAdopters);
            try {
                JSONArray jsonArray = new JSONArray(JsonValue);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject channelData = jsonArray.getJSONObject(i);
                    title.add(channelData.getString("title"));
                    desc.add(channelData.getString("desc"));
                    image.add(channelData.getString("thumbnail"));
                    link.add(channelData.getString("link"));
                    shortsAdopters.notifyDataSetChanged();
                    Log.d(TAG, "1onErrorResponse: " + channelData.getString("desc"));
                }

            } catch (JSONException e) {
                Log.d(TAG, "1onErrorResponse: " + "channelData.getString()");
                throw new RuntimeException(e);
            }
        }

    }

}