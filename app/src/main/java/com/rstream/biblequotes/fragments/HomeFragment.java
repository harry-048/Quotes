package com.rstream.biblequotes.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rstream.biblequotes.MainActivity;
import com.rstream.biblequotes.QuotesNames;
import com.rstream.biblequotes.QuotesTypes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rstream.biblequotes.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    ProgressBar progressBar;

    private View rootView;

    ArrayList<QuotesNames> quotesNames;

    InterstitialAd mInterstitialAd;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.AdUnitIdProduct));
       /* //mInterstitialAd.setAdUnitId(getString(R.string.AdUnitId));
        if (BuildConfig.DEBUG)
            mInterstitialAd.setAdUnitId(getString(R.string.AdUnitId));
        else
            mInterstitialAd.setAdUnitId(getString(R.string.AdUnitIdProduct));*/
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
        final QuotesTypes quotesTypes = new QuotesTypes(getActivity(),MainActivity.images, MainActivity.motivationName,mInterstitialAd,MainActivity.width);
        recyclerView.setAdapter(quotesTypes);
    }

}
