package com.example.quotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteQuotes extends RecyclerView.Adapter<QuotesViewHolder> {

    public FavoriteQuotes(Context mContext) {
        Log.d("favoritecon","in favorites"+mContext);
        this.mContext = mContext;
    }

    Context mContext;
    SharedPreferences sharedPreferences;
    ArrayList<String> quotesList = new ArrayList<String>();
    Set<String> set;

    @NonNull
    @Override
    public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*sharedPreferences = mContext.getSharedPreferences("prefs.xml",MODE_PRIVATE);
        set= sharedPreferences.getStringSet("likedImages",null);
        Log.d("favorite","in favorites"+set);
        if (set==null){
            set = new HashSet<String>();
        }
        else {
            quotesList.addAll(set);
        }*/
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesViewHolder quotesViewHolder, int i) {
       /* final String imgUrl=quotesList.get(i);
        Log.d("favoriteimage",imgUrl);*/
    }

    @Override
    public int getItemCount() {
       // Log.d("favoritecount","in favorites"+quotesList.size());
        return 1;
    }
}
