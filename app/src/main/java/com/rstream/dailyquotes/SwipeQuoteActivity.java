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
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rstream.dailyquotes.R;
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
    String motivationType;
    int imagePosition;
    String intentClassName;
    DiscreteScrollView scrollView;
    public static ArrayList<String> images;
    JSONObject data;


    private void parseData() {
        InputStream in = getResources().openRawResource(R.raw.data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        try {
            data = new JSONObject(jsonString);
            /*Intent intent = new Intent(this,MyFirebaseMessagingService.class);
            intent.putExtra("jsonfile",data+"");*/
            Iterator<String> iter = data.keys();
            while (iter.hasNext()) {
                String key = iter.next();

                try {
                    String[] words = key.split(" ");
                    StringBuilder sb = new StringBuilder();
                    if (words[0].length() > 0) {
                        sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
                        for (int i = 1; i < words.length; i++) {
                            sb.append(" ");
                            sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                        }
                    }
                    String name = sb.toString();
                    /*if (clicked){

                        clicked=false;
                    }*/
                    //  String[] imgurl=data.get("happy").toString();
                    //String[] imgurl = Arrays.copyOf(data.get("happy"), data.get("happy").l, String[].class);
                    // quoteskeyvalue.add(new QuotesKeyVal(name, key));
                    //quotesImages.add(new QuotesImages(data.get("happy")));

                    Object value = data.get(key);
                    //listItems.add(name);
                    //motivationName = quoteskeyvalue.get(0).quoteKey;
                    putImagetoArray();
                } catch (JSONException ee) {
                    // Something went wrong!
                    ee.printStackTrace();
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void putImagetoArray() {
        final JSONArray[] imagesArray = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    imagesArray[0] = data.getJSONArray(motivationType);
                    images = new ArrayList();
                    for (int i = 0; i < imagesArray[0].length(); i++) {
                        images.add(imagesArray[0].getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).run();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_quote);
        quotesImages = getIntent().getStringArrayListExtra("imageslist");
        motivationType = getIntent().getStringExtra("Type");
        imagePosition = Integer.parseInt(getIntent().getStringExtra("clickedImage"));
        intentClassName = getIntent().getStringExtra("className");
        scrollView = findViewById(R.id.picker);


        parseData();
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
        if (intentClassName.equals("FromFireBase")){
            final SwipeQuoteAdapter swipeQuotes = new SwipeQuoteAdapter(this,images,motivationType,imagePosition,scrollView);
            scrollView.setAdapter(swipeQuotes);
        }
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
