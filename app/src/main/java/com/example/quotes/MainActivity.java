package com.example.quotes;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quotes.fragments.FavoriteFragment;
import com.example.quotes.fragments.HomeFragment;
import com.example.quotes.fragments.SearchFragment;

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

public class MainActivity extends AppCompatActivity {

    JSONObject data;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    public static ArrayList<String> images;
    String url="";
    String jsonurl="";
    public static String motivationName ="";
    boolean clicked=false;
    HomeFragment homeFragment = new HomeFragment();
    Fragment favoriteFragment = new FavoriteFragment();
    SearchFragment searchFragment = new SearchFragment();
    Fragment selectedFragment = homeFragment;
    private DrawerLayout drawerLayout;
    ArrayList<QuotesNames> quotesNames;
    private ArrayList<QuotesKeyVal> quoteskeyvalue;
    private ArrayList<QuotesImages> quotesImages;
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = homeFragment;
                   /* url="http://52.91.243.194/RIA/set/setgrid.php?type=category&page=moving-light&country=us&lang=en";
                    DownloadQuote getQuote = new DownloadQuote();
                    getQuote.execute(url);*/
                    // mTextMessage.setText(R.string.title_home);
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment= favoriteFragment;
                    //  mTextMessage.setText(R.string.title_dashboard);
                    break;
                case R.id.navigation_notifications:
                    selectedFragment=searchFragment;
                    // mTextMessage.setText(R.string.title_notifications);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        quoteskeyvalue = new ArrayList<>();
        quotesImages = new ArrayList<>();
        listItems=new ArrayList<String>();
        listView=(ListView) findViewById(R.id.nameslistView);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        listView.setAdapter(adapter);



        parseData();


        quotesNames = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();


        NavigationView navigationView =(NavigationView) findViewById(R.id.nav_view);

       /* motivationName=quoteskeyvalue.get(0).getQuoteKey();
        putImagetoArray();
        setRecycleView();*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clicked=true;
                QuotesKeyVal quotesKeyVal = quoteskeyvalue.get(position);
                motivationName =quotesKeyVal.quoteKey;
                putImagetoArray();
                Toast.makeText(MainActivity.this,quotesKeyVal.getFormatedName()+","+quotesKeyVal.quoteKey , Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                setRecycleView();



            }
        });


    }

    private void setRecycleView() {

        selectedFragment=homeFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
        homeFragment.setRecycleView();
        navigation.setSelectedItemId(R.id.navigation_home);

    }

    private void parseData() {
        InputStream in = getResources().openRawResource(R.raw.data);
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
            Iterator<String> iter = data.keys();
            while (iter.hasNext()) {
                String key = iter.next();

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
                    /*if (clicked){

                        clicked=false;
                    }*/
                  //  String[] imgurl=data.get("happy").toString();
                    //String[] imgurl = Arrays.copyOf(data.get("happy"), data.get("happy").l, String[].class);
                    quoteskeyvalue.add(new QuotesKeyVal(name,key));
                    //quotesImages.add(new QuotesImages(data.get("happy")));

                    Object value = data.get(key);
                    listItems.add(name);
                    Log.d("categoryin",key+","+ motivationName);
                    Log.d(key,data.get(key).toString());
                    Log.d("motivationinparse", motivationName);
                    motivationName =quoteskeyvalue.get(0).quoteKey;
                    putImagetoArray();
                } catch (JSONException ee) {
                    // Something went wrong!
                    ee.printStackTrace();
                }
            }
            searchFragment.setListItems(listItems);
            motivationName =quoteskeyvalue.get(0).quoteKey;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void putImagetoArray() {
        JSONArray imagesArray = null;
        try {
            imagesArray = data.getJSONArray(motivationName);
            images = new ArrayList();
            for (int i=0;i<imagesArray.length();i++){
                images.add(imagesArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}