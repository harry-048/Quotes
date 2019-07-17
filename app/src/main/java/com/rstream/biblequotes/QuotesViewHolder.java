package com.rstream.biblequotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rstream.biblequotes.R;



public class QuotesViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;


    public QuotesViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageView);

    }
}
