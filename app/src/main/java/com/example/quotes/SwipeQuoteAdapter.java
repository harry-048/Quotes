package com.example.quotes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

public class SwipeQuoteAdapter extends RecyclerView.Adapter<swipeViewHolder> {



    public SwipeQuoteAdapter(Context mContext, ArrayList<String> quotesImages, String motivationType, int imagePosition,DiscreteScrollView scrollView) {
        this.mContext = mContext;
        this.quotesImages = quotesImages;
        this.motivationType = motivationType;
        this.imagePosition = imagePosition;
        this.scrollView = scrollView;
    }

    Context mContext;
    ArrayList<String> quotesImages;
    String motivationType;
    int imagePosition;
    String imgUrl;
    DiscreteScrollView scrollView;
    @NonNull
    @Override
    public swipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.swipe_content, viewGroup, false);
        swipeViewHolder viewHolder = new swipeViewHolder(view);

        return viewHolder;
    }

    private void showImage(int imagePosition, swipeViewHolder viewHolder) {
        //Log.d("position",imagePosition+"b");
        imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(imagePosition);
        Picasso.get().load(imgUrl).into(viewHolder.imageView);

    }

    @Override
    public void onBindViewHolder(@NonNull swipeViewHolder swipeViewHolder, final int i) {

       // showImage(i,swipeViewHolder);
        imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(i);
        Picasso.get().load(imgUrl).into(swipeViewHolder.imageView);
      //  Log.d("positions",""+i);
        Intent intent = new Intent("message_subject_intent");
        intent.putExtra("QuoteImage" , imgUrl);
        intent.putExtra("postion",i);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);



       /* swipeViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("position",""+i);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return quotesImages.size();
    }
}
