package com.rstream.dailyquotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.rstream.dailyquotes.R;
import com.jgabrielfreitas.core.BlurImageView;


public class QuotesViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;


    public QuotesViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageView);

    }
}
