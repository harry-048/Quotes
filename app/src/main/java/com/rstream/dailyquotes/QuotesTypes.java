package com.rstream.dailyquotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Callback;
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
    int width;




    public QuotesTypes(Context mContext, ArrayList<String> quotesImages, String motivationName, InterstitialAd mInterstitialAd, int width) {
        this.mContext = mContext;
        this.quotesImages=quotesImages;
        this.motivationName=motivationName;
        this.mInterstitialAd = mInterstitialAd;
        this.width=width;
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
        final boolean[] imageload = {false};
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
                            if (imageload[0])
                                mContext.startActivity(intent);
                            else
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                                alertDialog.setTitle("Check your internet connection");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    } else {

                        if (imageload[0])
                            mContext.startActivity(intent);
                        else
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                            alertDialog.setTitle("Check your internet connection");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
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

                    if (imageload[0])
                        mContext.startActivity(intent);
                    else
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setTitle("Check your internet connection");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }




            }
        });

        try{
            int h = (width/2)-16;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,h);
            quotesViewHolder.imageView.setLayoutParams(parms);
            Picasso.get().load(imgUrl).placeholder(mContext.getResources().getDrawable(R.drawable.loadinganimation)).into(quotesViewHolder.imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageload[0] =true;
                }

                @Override
                public void onError(Exception e) {
                    imageload[0] =false;

                }
            });
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
