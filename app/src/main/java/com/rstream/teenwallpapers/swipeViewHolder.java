package com.rstream.teenwallpapers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


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
