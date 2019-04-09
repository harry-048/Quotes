package com.example.quotes;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

        set= sharedPreferences.getStringSet("likedImages",null);
        if (set==null){
           // Toast.makeText(this, "its null", Toast.LENGTH_SHORT).show();
            set = new HashSet<String>();
        }
        showImage(imagePosition);
        checkLike();

        imageView.setOnTouchListener(new OnSwipeTouchListener(ImagePopup.this) {
            public void onSwipeTop() {
               // Toast.makeText(ImagePopup.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                if (imagePosition>0){
                    imagePosition--;
                    showImage(imagePosition);
                    checkLike();
                }
               // Toast.makeText(ImagePopup.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                if (imagePosition<quotesImages.size()-1){
                    imagePosition++;
                    showImage(imagePosition);
                    checkLike();
                }
              //  Toast.makeText(ImagePopup.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
               // Toast.makeText(ImagePopup.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

       /* for (int i=0;i<likedImages.size();i++){
            if (imgUrl==likedImages.get(i)){

            }
            else {
                likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
            }
        }*/

        likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLike()){
                    Log.d("Error","is here");
                    set.add(imgUrl);
                    sharedPreferences.edit().putStringSet("likedImages",set).apply();
                  //  Log.d("valuesets",set.size()+"");
                    //likedImages.add(imgUrl);
                    likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
                }
                else {
                    set.remove(imgUrl);
                   // Log.d("valueset",set.size()+"");
                    sharedPreferences.edit().putStringSet("likedImages",set).apply();
                    likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
            }
        });

        Log.d("click",likedImages+"");
        Log.d("clicke",motivationType+"");
        Log.d("clicked",imagePosition+"");
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
       // Glide.with(this).load(imgUrl).into(imageView);
        Picasso.get().load(imgUrl).into(imageView);
        Log.d("click",set+"");
    }


}
