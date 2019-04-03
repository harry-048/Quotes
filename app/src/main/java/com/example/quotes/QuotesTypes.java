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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class QuotesTypes extends RecyclerView.Adapter<QuotesViewHolder> {

    private ArrayList<QuotesNames> dataSet;
    Context mContext;
    String jsonurl;



    public QuotesTypes(ArrayList<QuotesNames> dataSet, Context mContext, String jsonurl) {
        this.dataSet = dataSet;
        this.mContext = mContext;
        this.jsonurl = jsonurl;
        Log.d("inside contructor","here,"+dataSet);
        Log.d("inside contructors","here,"+mContext);
    }



    @NonNull
    @Override
    public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.quotes_content, viewGroup, false);
        Log.d("inside view holder","here");
        QuotesViewHolder viewHolder = new QuotesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final QuotesViewHolder quotesViewHolder, int i) {
        final QuotesNames quotesNames = dataSet.get(i);
        Log.d("data viewholder",quotesNames.getQuoteName()+",aa");
        try{
            Log.d("vannundo ","unde");
            JSONObject jsonObject = new JSONObject(jsonurl);
            String category = jsonObject.getString("category");
            if (category!=null){
                JSONArray arr = new JSONArray(category);

                    JSONObject jsonpart = arr.getJSONObject(i);
                    quotesViewHolder.quotesname.setText(jsonpart.getString("author"));
                    Log.d("Author "+i,jsonpart.getString("author"));
                    Log.d("Image "+i,jsonpart.getString("url"));

                Glide.with(mContext).load(jsonpart.getString("url")).
                        into(quotesViewHolder.imageView);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }





    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }
}
