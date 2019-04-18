package com.rstream.dailyquotes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rstream.dailyquotes.MainActivity;
import com.rstream.dailyquotes.QuotesNames;
import com.rstream.dailyquotes.QuotesTypes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rstream.dailyquotes.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    boolean pass=true;
    String url="";
    String jsonurl="";
    int flag=0;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager layoutManager;
    ProgressBar progressBar;

    private View rootView;

    ArrayList<QuotesNames> quotesNames;

    InterstitialAd mInterstitialAd;

    public HomeFragment() {
        // Required empty public constructor
        Log.d("HomeFragment","Created");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.AdUnitId));
        //mInterstitialAd.setAdUnitId("ca-app-pub-9098946909579213/8837571799");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        setRecycleView();




        try{


        }
        catch (Exception ee){
            Log.d("recycle error","here, "+ee.getMessage());
            ee.printStackTrace();
        }



        return rootView;

    }



    public void setRecycleView() {
        recyclerView = rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        quotesNames = new ArrayList<>();

        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        final QuotesTypes quotesTypes = new QuotesTypes(getActivity(),MainActivity.images, MainActivity.motivationName,mInterstitialAd);
        recyclerView.setAdapter(quotesTypes);
    }

}
