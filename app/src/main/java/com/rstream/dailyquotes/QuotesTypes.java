package com.rstream.dailyquotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class QuotesTypes extends RecyclerView.Adapter<QuotesViewHolder> {


    private ArrayList<String> quotesImages;
    Context mContext;
    String motivationName;
    SharedPreferences preferences;
    public boolean adShowingFlag = true;
    private InterstitialAd mInterstitialAd;
    boolean purchased =false;




    public QuotesTypes(Context mContext, ArrayList<String> quotesImages, String motivationName, InterstitialAd mInterstitialAd) {
        this.mContext = mContext;
        this.quotesImages=quotesImages;
        this.motivationName=motivationName;
        this.mInterstitialAd = mInterstitialAd;
    }

    @NonNull
    @Override
    public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.quotes_content, viewGroup, false);
        QuotesViewHolder viewHolder = new QuotesViewHolder(view);


        preferences = mContext.getSharedPreferences("prefs.xml",MODE_PRIVATE);
        adShowingFlag= preferences.getBoolean("adShowingFlag",true);
        purchased = preferences.getBoolean("purchased",false);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final QuotesViewHolder quotesViewHolder, final int i) {

        final String imgUrl=mContext.getString(R.string.imagelink)+motivationName+"/"+ quotesImages.get(i);
        quotesViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Intent intent = new Intent(mContext,SwipeQuoteActivity.class);
                intent.putExtra("imageslist",quotesImages);
                intent.putExtra("clickedImage",i+"");
                intent.putExtra("Type",motivationName);
                intent.putExtra("className","QuotesTypes");

                if (!purchased){
                    if (mInterstitialAd.isLoaded()) {
                        if (adShowingFlag){
                            mInterstitialAd.show();
                        }
                        else {
                            adShowingFlag=true;
                            preferences.edit().putBoolean("adShowingFlag",adShowingFlag).apply();
                            mContext.startActivity(intent);
                        }
                    } else {

                        mContext.startActivity(intent);
                    }
                    mInterstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed() {
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            adShowingFlag=false;
                            preferences.edit().putBoolean("adShowingFlag",adShowingFlag).apply();
                            mContext.startActivity(intent);
                            super.onAdClosed();
                        }
                    });
                }
                else {
                    mContext.startActivity(intent);
                }




            }
        });

        try{
            Picasso.get().load(imgUrl).placeholder(mContext.getResources().getDrawable(R.drawable.loadinganimation)).into(quotesViewHolder.imageView);
        }
        catch (Exception e){
            Toast.makeText(mContext, "No image found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }





    }

    @Override
    public int getItemCount()
    {
        return quotesImages.size();
    }
}
