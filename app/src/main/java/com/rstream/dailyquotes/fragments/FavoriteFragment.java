package com.rstream.dailyquotes.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rstream.dailyquotes.FavoriteQuotes;
import com.rstream.dailyquotes.R;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    Set<String> set;
    ArrayList<String> quotesList = new ArrayList<String>();


    private View rootView;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

      /*  sharedPreferences = getActivity().getSharedPreferences("prefs.xml",MODE_PRIVATE);
        new HashSet<String>();
        set= sharedPreferences.getStringSet("likedImages",null);
        if (set==null){
            Log.d("favnull", "onCreateViewHolder:");
            set = new HashSet<String>();
        }
        else {
            Log.d("favorite","in favorites"+set);
            quotesList.clear();
            quotesList.addAll(set);
        }
        Log.d("fav",set.size()+",");

        setRecycleView();*/

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getActivity().getSharedPreferences("prefs.xml",MODE_PRIVATE);
        new HashSet<String>();
        set= sharedPreferences.getStringSet("likedImages",null);
        if (set==null){
            Log.d("favnull", "onCreateViewHolder:");
            set = new HashSet<String>();
        }
        else {
            Log.d("favorite","in favorites"+set);
            quotesList.clear();
            quotesList.addAll(set);
        }
        Log.d("fav",set.size()+",");

        setRecycleView();
    }



    private void setRecycleView() {
        recyclerView = rootView.findViewById(R.id.fav_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        final FavoriteQuotes favoriteQuotes = new FavoriteQuotes(getActivity(),quotesList);
        recyclerView.setAdapter(favoriteQuotes);
    }
}
