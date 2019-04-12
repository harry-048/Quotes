package com.example.quotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 //   ArrayList<String> quotesList = new ArrayList<String>();


    @NonNull
    @Override
    public FavoritesQuotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fav_quotes_content, viewGroup, false);
        FavoritesQuotesViewHolder viewHolder = new FavoritesQuotesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesQuotesViewHolder favoritesQuotesViewHolder, int i) {
        final String imgUrl=set.get(i);
        Picasso.get().load(imgUrl).into(favoritesQuotesViewHolder.imageView);
        Picasso.get().load(imgUrl)
                .transform(new BlurTransformation(mContext))
                .into(favoritesQuotesViewHolder.blurImageView);
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

