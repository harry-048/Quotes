package com.rstream.teenwallpapers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;




class FavoritesQuotesViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    ImageView blurImageView;

    FavoritesQuotesViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageView);
        blurImageView = itemView.findViewById(R.id.backBlurImageView);
    }
}
