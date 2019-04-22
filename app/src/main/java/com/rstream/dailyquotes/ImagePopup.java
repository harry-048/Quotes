package com.rstream.dailyquotes;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ImagePopup extends AppCompatActivity {

    ArrayList<String> quotesImages;
    ArrayList<String> likedImages;
    Set<String> set;
    String motivationType;
    int imagePosition;
    ImageView imageView;
    ImageView likeImageView;
    String imgUrl;
    int f=2;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_popup);

        sharedPreferences = getSharedPreferences("prefs.xml",MODE_PRIVATE);

        likedImages = new ArrayList<>();
        imageView= findViewById(R.id.quoteImageView);
        likeImageView = findViewById(R.id.likeImageView);
        quotesImages = getIntent().getStringArrayListExtra("imageslist");
        motivationType = getIntent().getStringExtra("Type");
        imagePosition = Integer.parseInt(getIntent().getStringExtra("clickedImage"));
        set = new HashSet<String>();
        set= sharedPreferences.getStringSet("likedImages",null);
        f= sharedPreferences.getInt("flag",0);
        if (set==null){
            set = new HashSet<String>();
        }
        showImage(imagePosition);
        checkLike();


        imageView.setOnTouchListener(new OnSwipeTouchListener(ImagePopup.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                if (imagePosition>0){
                    imagePosition--;
                    showImage(imagePosition);
                    checkLike();
                }
            }
            public void onSwipeLeft() {
                if (imagePosition<quotesImages.size()-1){
                    imagePosition++;
                    showImage(imagePosition);
                    checkLike();
                }
            }
            public void onSwipeBottom() {
            }

        });



        likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLike()){

                    set.add(imgUrl);
                    f++;
                    sharedPreferences.edit().putInt("flag",f).apply();
                    sharedPreferences.edit().putStringSet("likedImages",set).apply();

                    likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
                }
                else {

                    set.remove(imgUrl);
                    f--;
                    sharedPreferences.edit().putInt("flag",f).apply();
                    sharedPreferences.edit().putStringSet("likedImages",set).apply();

                    likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
            }
        });

    }

    private boolean checkLike() {
        if (set!=null){
            if(set.contains(imgUrl)){

                likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
                return true;
            }
            else {
                likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                return false;
            }
        }
        return false;
    }

    private void showImage(int imagePosition) {
        imgUrl=this.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(imagePosition);
        Picasso.get().load(imgUrl).into(imageView);
    }


}
