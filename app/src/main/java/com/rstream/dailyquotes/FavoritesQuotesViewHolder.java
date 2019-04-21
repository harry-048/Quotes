package com.rstream.dailyquotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rstream.dailyquotes.R;


public class FavoritesQuotesViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    ImageView blurImageView;

    public FavoritesQuotesViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageView);
        blurImageView = itemView.findViewById(R.id.backBlurImageView);
    }
}
