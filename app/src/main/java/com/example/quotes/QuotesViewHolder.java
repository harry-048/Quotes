package com.example.quotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class QuotesViewHolder extends RecyclerView.ViewHolder {
    TextView quotesname;
    ImageView imageView;

    public QuotesViewHolder(@NonNull View itemView) {
        super(itemView);
        Log.d("anything","here");
        quotesname=itemView.findViewById(R.id.textView);
        imageView=itemView.findViewById(R.id.imageView);
    }
}
