package com.example.quotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class swipeViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    ImageView likeImageView;
    ImageView downloadImageView;
    ImageView shareImageView;

    public swipeViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.swipeQuoteImageView);
        likeImageView = itemView.findViewById(R.id.swpieLikeImageView);
        downloadImageView = itemView.findViewById(R.id.swipeDownloadImageView);
        shareImageView = itemView.findViewById(R.id.swipeShareImageView);
    }
}
