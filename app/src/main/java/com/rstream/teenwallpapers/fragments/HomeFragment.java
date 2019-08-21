package com.rstream.teenwallpapers.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rstream.teenwallpapers.MainActivity;
import com.rstream.teenwallpapers.QuotesNames;
import com.rstream.teenwallpapers.QuotesTypes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rstream.teenwallpapers.R;
import com.rstream.teenwallpapers.SetDailyWallpaper;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    ProgressBar progressBar;
    FloatingActionButton fab;
    SharedPreferences sharedPreferences;

    private View rootView;

    ArrayList<QuotesNames> quotesNames;

    InterstitialAd mInterstitialAd;
    MainActivity mainActivity;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = new MainActivity();

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
        fab = rootView.findViewById(R.id.floatingActionButton);

        try {
            sharedPreferences = getContext().getSharedPreferences("prefs.xml", Context.MODE_PRIVATE);
            String lastWallpaperName = sharedPreferences.getString("wallpaperName","");

            if (lastWallpaperName!=null && lastWallpaperName.toLowerCase().contains(MainActivity.motivationName)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_24dp));
            }
            // fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_done_black_24dp));
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_wallpaper_black_24dp));
            }
            // fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_wallpaper_black_24dp));
            final SearchFragment searchFragment = new SearchFragment();

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lastWallpaperName = sharedPreferences.getString("wallpaperName","");
                    if (lastWallpaperName!=null && lastWallpaperName.toLowerCase().contains(MainActivity.motivationName)){
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("To stop changing wallpaper every day, tap Continue");

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CONTINUE",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_wallpaper_black_24dp));

                                        String oldName = sharedPreferences.getString("wallpaperName","");

                                        if (oldName!=null && !oldName.equals("") ){
                                            Log.d("settingwallpaper","oldname is "+oldName + "position is "+sharedPreferences.getInt("wallpaperPosition",1));
                                            MainActivity.listItems.set(sharedPreferences.getInt("wallpaperPosition",1),oldName);
                                        }


                                        searchFragment.setListItems(MainActivity.listItems);
                                        MainActivity.adapter.notifyDataSetChanged();

                                        sharedPreferences.edit().putString("wallpaperName","").apply();
                                        sharedPreferences.edit().putBoolean("setDailyWallpaper",false).apply();
                                        dialog.dismiss();

                                    }
                                });




                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getContext().getResources().getColor(R.color.Textcolor));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                            }
                        });

                        alertDialog.show();

                        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                        textView.setTextSize(16);
                        // textView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

                    }
                    else {
                        Log.d("settingwallpaper","inside else ");
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Wallpaper will change automatically every day. To finsih setup, tap CONTINUE");
                        //alertDialog.setTitle("Get a different wallpaper from "+MainActivity.motivationName+" category every day!");
                        //   alertDialog.setTitle("Wallpaper wil change automatically every day. To finsih setup, tap Continue");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        sharedPreferences.edit().putBoolean("setDailyWallpaper",false).apply();
                                        dialog.dismiss();
                                    }
                                });


                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CONTINUE",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_24dp));

                                        for (int i=0;i<MainActivity.listItems.size();i++){
                                            if (MainActivity.listItems.get(i).toLowerCase().contains(MainActivity.motivationName)){

                                                String oldName = sharedPreferences.getString("wallpaperName","");

                                                if (oldName!=null && !oldName.equals("") ){
                                                    MainActivity.listItems.set(sharedPreferences.getInt("wallpaperPosition",1),oldName);
                                                }

                                                String str = MainActivity.listItems.get(i);
                                                String cstr = MainActivity.listItems.get(i)+"  -  wallpaper";
                                                sharedPreferences.edit().putInt("wallpaperPosition",i).apply();
                                                sharedPreferences.edit().putString("wallpaperName",MainActivity.listItems.get(i)).apply();
                                                // MainActivity.listItems.get(i).concat("  -  wallpaper");
                                                MainActivity.listItems.set(i,cstr);
                                                // MainActivity.listItems.get(i).replace(MainActivity.listItems.get(i),MainActivity.listItems.get(i)+"  -  wallpaper");
                                                searchFragment.setListItems(MainActivity.listItems);
                                                MainActivity.adapter.notifyDataSetChanged();
                                                Log.d("listitemdata",MainActivity.listItems.get(i)+"");
                                            }
                                            Log.d("listitemdata","item no "+i +MainActivity.listItems.get(i)+" , "+MainActivity.motivationName);
                                        }


                                        sharedPreferences.edit().putBoolean("setDailyWallpaper",true).apply();

                                        sharedPreferences.edit().putInt("daycount",0).apply();
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(Calendar.HOUR_OF_DAY, 6);
                                        calendar.set(Calendar.MINUTE, 0);
                                        calendar.set(Calendar.SECOND, 0);

                                        Log.d("calendartime","morning "+calendar.getTimeInMillis()+" , "+calendar.getTime() );

                                        Intent notifyIntent = new Intent(getContext(), SetDailyWallpaper.class);
                                        notifyIntent.putExtra("screenwidth",MainActivity.width);
                                        notifyIntent.putExtra("screenheight",MainActivity.height);
                                        sharedPreferences.edit().putString("notificationType",MainActivity.motivationName).apply();
                                        //  notifyIntent.putExtra("motivationname",MainActivity.motivationName);
                                        notifyIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 101, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        AlarmManager dawnalarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                                        //dawnalarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
                                        dawnalarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);



                                        dialog.dismiss();

                                    }
                                });




                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getContext().getResources().getColor(R.color.Textcolor));
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                            }
                        });

                        alertDialog.show();

                        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                        textView.setTextSize(16);
                        // textView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));

                    }



                }
            });
        }
        catch (NullPointerException npe){
            Log.d("errorsettinwallpaper","null error is "+npe);
            npe.printStackTrace();
        }
        catch (Exception e){
            Log.d("errorsettinwallpaper","error is "+e);
            e.printStackTrace();
        }

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
        final QuotesTypes quotesTypes = new QuotesTypes(getActivity(),MainActivity.images,MainActivity.thumbsImages, MainActivity.motivationName,mInterstitialAd,MainActivity.width);
        recyclerView.setAdapter(quotesTypes);
    }

}
