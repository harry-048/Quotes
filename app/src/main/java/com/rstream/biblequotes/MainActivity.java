package com.rstream.biblequotes;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rstream.biblequotes.fragments.FavoriteFragment;
import com.rstream.biblequotes.fragments.HomeFragment;
import com.rstream.biblequotes.fragments.SearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener,NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<String> images;
    public static ArrayList<String> thumbsImages;
    public static String motivationName = "";
    JSONObject data;
    public static ArrayList<String> listItems;
    public static ArrayAdapter<String> adapter;
    ListView listView;
    boolean clicked = false;
    HomeFragment homeFragment = new HomeFragment();
    Fragment favoriteFragment = new FavoriteFragment();
    SearchFragment searchFragment = new SearchFragment();
    Fragment selectedFragment = searchFragment;
    ArrayList<QuotesNames> quotesNames;
    BottomNavigationView navigation;
    private DrawerLayout drawerLayout;
    private ArrayList<QuotesKeyVal> quoteskeyvalue;
    private InterstitialAd mInterstitialAd;
    private BillingClient billingClient;
    SharedPreferences sharedPreferences;
    boolean isFirstOpen = true;
    boolean purchased =false;
    boolean sixmonths = false;
    boolean threeDayTrial = false;
    public static int width=0;
    public static int height=0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = homeFragment;
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = favoriteFragment;
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = searchFragment;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Log.d("sharedPreferences","its here");
        sharedPreferences.edit().putBoolean("adShowingFlag",true).apply();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            if (selectedFragment==favoriteFragment || selectedFragment==searchFragment)
            {
                selectedFragment=homeFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                navigation.setSelectedItemId(R.id.navigation_home);
            }
            else if (selectedFragment==homeFragment){
                if (!purchased && !sixmonths && !threeDayTrial)
                    mInterstitialAd.show();
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Are you sure to exit app?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "EXIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferences.edit().putBoolean("adShowingFlag",true).apply();
                                dialog.dismiss();
                                finish();
                            }
                        });

                alertDialog.show();
            }
        }

    }

    private void initializeBillingClient(){
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    refreshPurchaseList();

                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

      /*  billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    refreshPurchaseList();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

       // setSelectedItemColor(0);
        if (billingClient.isReady()){
            refreshPurchaseList();
        }
    }

    private void refreshPurchaseList() {
        Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        List<Purchase> purchasedList = purchasesResult.getPurchasesList();
        for(Purchase purchase: purchasedList){
            if (purchase.getSku().equals(this.getString(R.string.premium_sku)))
            {
                purchased=true;
                sharedPreferences.edit().putBoolean("purchased",purchased).apply();
            }
        }
    }

    /*protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        if (data!=null)
            Log.d("appIndexingishere","data: "+data);
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            //do your action
            Log.d("appIndexingishere","yes");
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        //onNewIntent(getIntent());
        /*FirebaseMessaging.getInstance().subscribeToTopic("com.rstream.dailyquotes")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                    }
                });*/
       // FirebaseMessaging.getInstance().subscribeToTopic("messagecheckdailyquotes")
        //FirebaseMessaging.getInstance().subscribeToTopic("com.rstream.dailyquotes")
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.package_name))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });




        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.AdUnitIdProduct));
       /* if (BuildConfig.DEBUG)
            mInterstitialAd.setAdUnitId(getString(R.string.AdUnitId));
        else
            mInterstitialAd.setAdUnitId(getString(R.string.AdUnitIdProduct));*/
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        sharedPreferences = getSharedPreferences("prefs.xml",MODE_PRIVATE);
        sharedPreferences.edit().putInt("screenWidth",width).apply();
        sharedPreferences.edit().putInt("screenHeight",height).apply();
        purchased=sharedPreferences.getBoolean("purchased",false);
        sixmonths = sharedPreferences.getBoolean("sixMonthSubscribed",false);
        threeDayTrial = sharedPreferences.getBoolean("monthlySubscribed",false);
        sharedPreferences.edit().putBoolean("appOpened", true).apply();
        initializeBillingClient();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
            }
        }

        quoteskeyvalue = new ArrayList<>();
        listItems = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.nameslistView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("quoteitems", listItems);
        searchFragment.setArguments(bundle);


        parseData();

        /*adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);*/
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setSelectedItemColor(0);
            }
        }, 500);



        quotesNames = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        isFirstOpen=sharedPreferences.getBoolean("isFirstOpen",true);
        if (isFirstOpen)
        {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        sharedPreferences.edit().putBoolean("isFirstOpen",false).apply();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // for selecting an item
        navigation.getMenu().findItem(R.id.navigation_notifications).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectedItemColor(position);

                clicked = true;
                QuotesKeyVal quotesKeyVal = quoteskeyvalue.get(position);
                motivationName = quotesKeyVal.quoteKey;
                putImagetoArray();
                drawerLayout.closeDrawer(GravityCompat.START);
                setRecycleView();

            }
        });


    }

    private void setSelectedItemColor(int position) {

        for (int i=0;i<listView.getChildCount();i++){

            if (position==i){

                listView.getChildAt(i).setBackgroundResource(R.color.selected_color);
                ((TextView)listView.getChildAt(i)).setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
            }
            else {

                listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                ((TextView)listView.getChildAt(i)).setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.black));
            }
        }
    }


    public void clickListView(int position) {
        setSelectedItemColor(position);
        clicked = true;
        QuotesKeyVal quotesKeyVal = quoteskeyvalue.get(position);
        motivationName = quotesKeyVal.quoteKey;
        putImagetoArray();
        drawerLayout.closeDrawer(GravityCompat.START);
        setRecycleView();
    }

    private void setRecycleView() {

        selectedFragment = homeFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        homeFragment.setRecycleView();
        navigation.setSelectedItemId(R.id.navigation_home);

    }

    private void parseData() {
        InputStream in = null;
        if (getResources().getString(R.string.app_name).contains("Bible"))
            in = getResources().openRawResource(R.raw.bibledata);
        else if (getResources().getString(R.string.app_name).contains("Teen"))
            in = getResources().openRawResource(R.raw.teenwallpaper);
        else
            in = getResources().openRawResource(R.raw.data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        try {
            data = new JSONObject(jsonString);

            //Log.d("jsonobjectdata", data.length()+" is length , ");
            Iterator<String> iter = data.keys();
            while (iter.hasNext()) {
                //Log.d("jsonobjectdata", " key is "+ iter.next());
                String key = iter.next();
                Log.d("jsonobjectdata", " key is "+ key);
                try {
                    String[] words = key.split(" ");
                    StringBuilder sb = new StringBuilder();
                    if (words[0].length() > 0) {
                        sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
                        for (int i = 1; i < words.length; i++) {
                            sb.append(" ");
                            sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                        }
                    }
                    String name = sb.toString();
                    quoteskeyvalue.add(new QuotesKeyVal(name, key));
                    Object value = data.get(key);
                    listItems.add(name);

                    Log.d("categoryin", key + "," + motivationName+ ",name: " +name);
                    Log.d(key, data.get(key).toString());
                    Log.d("motivationinparse", motivationName);
                    motivationName = quoteskeyvalue.get(0).quoteKey;
                    putImagetoArray();
                } catch (JSONException ee) {
                    ee.printStackTrace();
                }
            }
            searchFragment.setListItems(listItems);
            if (sharedPreferences.getBoolean("setDailyWallpaper",false)){
                int x = sharedPreferences.getInt("wallpaperPosition",1);
                String cstr = listItems.get(x)+"  -  wallpaper";
                // MainActivity.listItems.get(i).concat("  -  wallpaper");
                listItems.set(x,cstr);
                // MainActivity.listItems.get(i).replace(MainActivity.listItems.get(i),MainActivity.listItems.get(i)+"  -  wallpaper");
                searchFragment.setListItems(MainActivity.listItems);
            }
            motivationName = quoteskeyvalue.get(0).quoteKey;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void putImagetoArray() {
        final JSONArray[] imagesArray = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    imagesArray[0] = data.getJSONArray(motivationName);
                    images = new ArrayList();
                    thumbsImages = new ArrayList();
                    for (int i = 0; i < imagesArray[0].length(); i++) {
                        images.add(getString(R.string.imagelink)+motivationName+"/"+imagesArray[0].getString(i));
                        thumbsImages.add(getString(R.string.imagelink)+motivationName+"/thumbs/"+imagesArray[0].getString(i));
                        //images.add(imagesArray[0].getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).run();

    }

    /*@Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

    }
}
