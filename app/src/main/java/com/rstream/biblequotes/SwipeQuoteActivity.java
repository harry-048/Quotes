package com.rstream.biblequotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

public class SwipeQuoteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    ArrayList<String> quotesImages;
    ArrayList<String> quotesThumbImages;
    String motivationType="";
    int imagePosition=0;
    String intentClassName="";
    DiscreteScrollView scrollView;
    public static ArrayList<String> images;
    JSONObject data;
    private InterstitialAd mInterstitialAd;
    boolean adshow=false;
    boolean purchased =false;
    boolean sixmonths = false;
    boolean threeDayTrial = false;
    SharedPreferences sharedPreferences;
    Intent i;
    int width=0;
    int height=0;

  /*  @Override
    protected void onDestroy() {
        Log.d("sharedPreferences","its really here");
        sharedPreferences.edit().putBoolean("adShowingFlag",true).apply();
        super.onDestroy();
    }
*/
    @Override
    public void onBackPressed() {

        if (intentClassName.equals("MyFirebaseMessaging")){
            i = new Intent(this,MainActivity.class);
            if (mInterstitialAd.isLoaded()){

                mInterstitialAd.show();
            }
            else {

                startActivity(i);
                finish();
            }
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    startActivity(i);
                    finish();
                }

            });
        }
        else
            super.onBackPressed();
    }

    private void parseThumbnail(ArrayList<String> quotesImage) {
        int j=0;
        String s;
        String thumb;
        for (j=0;j<quotesImage.size();j++){
            s = quotesImage.get(j);
            String[] str = s.split(motivationType);
            Log.d("stringsplit",str.length+" , motivation type: "+motivationType+" ,str= "+str[0]+" ,s= "+s);
            if (str.length>2){
                Log.d("stringsplit",str.length+" , "+str[0]+" , "+str[1]+" , "+str[2]);
                //thumb=str[0]+motivationType+"/thumbs"+str[1];
                thumb=str[0]+motivationType+"/thumbs"+str[1]+motivationType+str[2];
            }
            else {
                Log.d("stringsplit",str.length+" , "+str[0]+" , "+str[1]);
                thumb=str[0]+motivationType+"/thumbs"+str[1];
            }

            quotesThumbImages.add(thumb);
            thumb="";
            s="";

        }
        for (int k=0;k<quotesThumbImages.size();k++){
            Log.d("stringnameis",quotesThumbImages.get(k)+" , "+quotesImage.get(k));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_quote);
        quotesThumbImages = new ArrayList<>();
        quotesImages = getIntent().getStringArrayListExtra("imageslist");
        motivationType = getIntent().getStringExtra("Type");
        imagePosition = Integer.parseInt(getIntent().getStringExtra("clickedImage"));
        intentClassName = getIntent().getStringExtra("className");
        scrollView = findViewById(R.id.picker);
        sharedPreferences = getSharedPreferences("prefs.xml",MODE_PRIVATE);
        purchased=sharedPreferences.getBoolean("purchased",false);
        sixmonths = sharedPreferences.getBoolean("sixMonthSubscribed",false);
        threeDayTrial = sharedPreferences.getBoolean("monthlySubscribed",false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.d("heightandwidth",height+","+width);
        if (intentClassName.equals("MyFirebaseMessaging"))
            Log.d("stringsplit","from firebase");
        else
            parseThumbnail(quotesImages);
       // parseData();
        showSwipeQuotes(quotesImages,motivationType,imagePosition);

        scrollView.scrollToPosition(imagePosition);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("message_subject_intent"));



    }



    public void showSwipeQuotes(ArrayList<String> quotesImages, String motivationType, int imagePosition){
        if (intentClassName.equals("QuotesTypes")){
            final SwipeQuoteAdapter swipeQuotes = new SwipeQuoteAdapter(this,quotesImages,quotesThumbImages,motivationType,imagePosition,scrollView,height,width);
            scrollView.setAdapter(swipeQuotes);
        }
        if (intentClassName.equals("FavoriteQuotes")){
            final SwipeQuoteAdapter swipeQuotes = new SwipeQuoteAdapter(this,quotesImages,quotesThumbImages,"",imagePosition,scrollView,height,width);
            scrollView.setAdapter(swipeQuotes);
        }
        if (intentClassName.equals("MyFirebaseMessaging")){
            final SwipeQuoteAdapter swipeQuotes = new SwipeQuoteAdapter(this,quotesImages,quotesImages,"",imagePosition,scrollView,height,width);
            scrollView.setAdapter(swipeQuotes);

            if (!purchased && !sixmonths && !threeDayTrial){
                Log.d("Tokenmessagedsas", "haha message!" );
                adshow=true;
                mInterstitialAd = new InterstitialAd(this);
                mInterstitialAd.setAdUnitId(getString(R.string.AdUnitIdProduct));
                /*if (BuildConfig.DEBUG)
                    mInterstitialAd.setAdUnitId(getString(R.string.AdUnitId));
                else
                    mInterstitialAd.setAdUnitId(getString(R.string.AdUnitIdProduct));*/
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }
       /* if (intentClassName.equals("FromFireBase")){
            final SwipeQuoteAdapter swipeQuotes = new SwipeQuoteAdapter(this,images,motivationType,imagePosition,scrollView,height,width);
            scrollView.setAdapter(swipeQuotes);
        }*/
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name= intent.getStringExtra("QuoteImage");
        }
    };

    private void setRecycleView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

    }


}
