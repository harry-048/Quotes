package com.example.quotes;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.example.quotes.fragments.FavoriteFragment;
import com.example.quotes.fragments.HomeFragment;
import com.example.quotes.fragments.SearchFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment = new HomeFragment();
    Fragment favoriteFragment = new FavoriteFragment();
    Fragment searchFragment = new SearchFragment();
    Fragment selectedFragment = homeFragment;
    private DrawerLayout drawerLayout;


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





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();


        NavigationView navigationView =(NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
    }

    /*            new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            // set item as selected to persist highlight
            int id = menuItem.getItemId();
            if (id == R.id.nav_camera) {
                Toast.makeText(getBaseContext(), "camera", Toast.LENGTH_SHORT).show();
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {
                Toast.makeText(getBaseContext(), "gallery", Toast.LENGTH_SHORT).show();

            }
            menuItem.setChecked(true);
            // close drawer when item is tapped
            drawerLayout.closeDrawers();

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here

            return true;
        }
    }
*/






}
