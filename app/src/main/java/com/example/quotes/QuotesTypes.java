package com.example.quotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class QuotesTypes extends RecyclerView.Adapter<QuotesViewHolder> {


    private ArrayList<String> quotesImages;
    Context mContext;
    String motivationName;


    public QuotesTypes(Context mContext, ArrayList<String> quotesImages,String motivationName) {
        this.mContext = mContext;
        this.quotesImages=quotesImages;
        this.motivationName=motivationName;
    }

    @NonNull
    @Override
    public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.quotes_content, viewGroup, false);
        QuotesViewHolder viewHolder = new QuotesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final QuotesViewHolder quotesViewHolder, final int i) {

        final String imgUrl=mContext.getString(R.string.imagelink)+motivationName+"/"+ quotesImages.get(i);
        quotesViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext,ImagePopup.class);
                intent.putExtra("imageslist",quotesImages);
                intent.putExtra("clickedImage",i+"");
                intent.putExtra("Type",motivationName);
                mContext.startActivity(intent);*/

                Intent intent = new Intent(mContext,SwipeQuoteActivity.class);
                intent.putExtra("imageslist",quotesImages);
                intent.putExtra("clickedImage",i+"");
                intent.putExtra("Type",motivationName);
                mContext.startActivity(intent);


            }
        });
        try{
            //quotesViewHolder.quotesname.setText(quotesNames.getQuoteName());
           // Glide.with(mContext).load(imgUrl).into(quotesViewHolder.imageView);
            Picasso.get().load(imgUrl).into(quotesViewHolder.imageView);
        }
        catch (Exception e){
            Toast.makeText(mContext, "No image found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }





    }

    @Override
    public int getItemCount()
    {
        return quotesImages.size();
    }
}
