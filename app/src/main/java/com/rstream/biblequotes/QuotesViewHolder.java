package com.rstream.biblequotes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


public class QuotesViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;


    public QuotesViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageView);

    }
}
