package com.rstream.dailyquotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rstream.dailyquotes.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

public class SwipeQuoteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    ArrayList<String> quotesImages;
    String motivationType;
    int imagePosition;
    String intentClassName;
    /*InterstitialAd mInterstitialAd;
    SharedPreferences preferences;
    public boolean adShowingFlag = true;
    boolean swapFlag=true;*/
    DiscreteScrollView scrollView;

   /* @Override
    public void finish() {
        if (swapFlag)
            adShowingFlag = !adShowingFlag;
        preferences.edit().putBoolean("adShowingFlag",adShowingFlag).apply();
        Log.d("adshowingas","showing"+","+adShowingFlag);
        super.finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_quote);
        quotesImages = getIntent().getStringArrayListExtra("imageslist");
        motivationType = getIntent().getStringExtra("Type");
        imagePosition = Integer.parseInt(getIntent().getStringExtra("clickedImage"));
        intentClassName = getIntent().getStringExtra("className");
        scrollView = findViewById(R.id.picker);


        Log.d("displayimages",motivationType+","+imagePosition+","+quotesImages);


        showSwipeQuotes(quotesImages,motivationType,imagePosition);

        scrollView.scrollToPosition(imagePosition);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("message_subject_intent"));



    }



    public void showSwipeQuotes(ArrayList<String> quotesImages, String motivationType, int imagePosition){
        if (intentClassName.equals("QuotesTypes")){
            final SwipeQuoteAdapter swipeQuotes = new SwipeQuoteAdapter(this,quotesImages,motivationType,imagePosition,scrollView);
            scrollView.setAdapter(swipeQuotes);
        }
        if (intentClassName.equals("FavoriteQuotes")){
            final SwipeQuoteAdapter swipeQuotes = new SwipeQuoteAdapter(this,quotesImages,"",imagePosition,scrollView);
            scrollView.setAdapter(swipeQuotes);
        }
        if (intentClassName.equals("MyFirebaseMessaging")){
            final SwipeQuoteAdapter swipeQuotes = new SwipeQuoteAdapter(this,quotesImages,motivationType,imagePosition,scrollView);
            scrollView.setAdapter(swipeQuotes);
        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name= intent.getStringExtra("QuoteImage");
            Log.d("scrollimg",name+"");
        }
    };

    private void setRecycleView() {
      //  recyclerView = findViewById(R.id.swipeRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
       // final SwipeQuoteAdapter swipeQuotes = new SwipeQuoteAdapter(this,quotesImages,motivationType,imagePosition);
      //  recyclerView.setAdapter(swipeQuotes);
    }


}
