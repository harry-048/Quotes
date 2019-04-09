package com.example.quotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteQuotes extends RecyclerView.Adapter<QuotesViewHolder> {

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
    public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fav_quotes_content, viewGroup, false);
        QuotesViewHolder viewHolder = new QuotesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesViewHolder quotesViewHolder, int i) {
        final String imgUrl=set.get(i);
        Picasso.get().load(imgUrl).into(quotesViewHolder.imageView);
        Log.d("favoriteimage","aa");
    }

    @Override
    public int getItemCount() {
        Log.d("favoritecount","in favorites"+set.size());
        return set.size();
    }
}
