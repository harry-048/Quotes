package com.rstream.dailyquotes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rstream.dailyquotes.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class FavoriteQuotes extends RecyclerView.Adapter<FavoritesQuotesViewHolder> {


    public FavoriteQuotes(Context mContext,ArrayList<String> set) {
        Log.d("favoritecon","in favorites"+mContext);
        this.mContext = mContext;
        this.set = set;
    }

    Context mContext;
    ArrayList<String> set;
    String likedMotivationName;
 //   ArrayList<String> quotesList = new ArrayList<String>();


    @NonNull
    @Override
    public FavoritesQuotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fav_quotes_content, viewGroup, false);
        FavoritesQuotesViewHolder viewHolder = new FavoritesQuotesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesQuotesViewHolder favoritesQuotesViewHolder, final int i) {
        final String imgUrl=set.get(i);
        Picasso.get().load(imgUrl).into(favoritesQuotesViewHolder.imageView);
        Picasso.get().load(imgUrl)
                .transform(new BlurTransformation(mContext))
                .into(favoritesQuotesViewHolder.blurImageView);

        favoritesQuotesViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(mContext,SwipeQuoteActivity.class);
                intent.putExtra("imageslist",set);
                intent.putExtra("clickedImage",i+"");
                intent.putExtra("className","FavoriteQuotes");
                String s = imgUrl.replace(mContext.getString(R.string.imagelink),"");
                String[] str = s.split("/");
                likedMotivationName = str[0];
                intent.putExtra("Type",likedMotivationName);
                mContext.startActivity(intent);
            }
        });
        //Picasso.get().load(imgUrl).into(favoritesQuotesViewHolder.blurImageView);
       // favoritesQuotesViewHolder.blurImageView.setBlur(15);

        Log.d("favoriteimage","aa");
    }

   /* @Override
    public void onBindViewHolder(@NonNull QuotesViewHolder quotesViewHolder, int i) {
        final String imgUrl=set.get(i);


        Picasso.get().load(imgUrl).into(quotesViewHolder.imageView);
        Log.d("favoriteimage","aa");
    }*/

    @Override
    public int getItemCount() {
        Log.d("favoritecount","in favorites"+set.size());
        return set.size();
    }
}

