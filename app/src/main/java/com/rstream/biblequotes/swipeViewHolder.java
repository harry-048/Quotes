package com.rstream.biblequotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rstream.biblequotes.R;



public class swipeViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    ImageView likeImageView;
    ImageView downloadImageView;
    ImageView shareImageView;
    ImageView blurImageView;
    ImageView wallpaperImageView;
    public swipeViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.swipeQuoteImageView);
        likeImageView = itemView.findViewById(R.id.swpieLikeImageView);
        downloadImageView = itemView.findViewById(R.id.swipeDownloadImageView);
        shareImageView = itemView.findViewById(R.id.swipeShareImageView);
        blurImageView = itemView.findViewById(R.id.BlurImageView);
        wallpaperImageView = itemView.findViewById(R.id.swpieWallpaperImageView);
    }
}
