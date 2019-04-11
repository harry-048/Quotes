package com.example.quotes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SwipeQuoteAdapter extends RecyclerView.Adapter<swipeViewHolder> {



    public SwipeQuoteAdapter(Context mContext, ArrayList<String> quotesImages, String motivationType, int imagePosition,DiscreteScrollView scrollView) {
        this.mContext = mContext;
        this.quotesImages = quotesImages;
        this.motivationType = motivationType;
        this.imagePosition = imagePosition;
        this.scrollView = scrollView;
    }

    Context mContext;
    ArrayList<String> quotesImages;
    String motivationType;
    int imagePosition;
    String imgUrl;
    DiscreteScrollView scrollView;
    boolean save = false;

    SharedPreferences sharedPreferences;
    Set<String> set;
    int f=2;

    @NonNull
    @Override
    public swipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.swipe_content, viewGroup, false);
        final swipeViewHolder viewHolder = new swipeViewHolder(view);

        sharedPreferences = mContext.getSharedPreferences("prefs.xml",MODE_PRIVATE);
        set = new HashSet<String>();
        set= sharedPreferences.getStringSet("likedImages",null);
        f= sharedPreferences.getInt("flag",0);
        if (set==null){
            set = new HashSet<String>();
        }
        checkLike(viewHolder);



        return viewHolder;
    }

    private boolean checkLike(swipeViewHolder viewHolder) {
        if (set!=null){
            if(set.contains(imgUrl)){

                viewHolder.likeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
                return true;
            }
            else {
                viewHolder.likeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                return false;
            }
        }
        return false;
    }

    private void showImage(int imagePosition, swipeViewHolder viewHolder) {
        //Log.d("position",imagePosition+"b");
        imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(imagePosition);
        Picasso.get().load(imgUrl).into(viewHolder.imageView);

    }

    @Override
    public void onBindViewHolder(@NonNull final swipeViewHolder swipeViewHolder, final int i) {

       // showImage(i,swipeViewHolder);
        imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(i);
        Picasso.get().load(imgUrl).into(swipeViewHolder.imageView);

        checkLike(swipeViewHolder);

        swipeViewHolder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLike(swipeViewHolder)){

                    set.add(imgUrl);
                    f++;
                    sharedPreferences.edit().putInt("flag",f).apply();
                    sharedPreferences.edit().putStringSet("likedImages",set).apply();

                    //likedImages.add(imgUrl);
                    swipeViewHolder.likeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
                }
                else {

                    set.remove(imgUrl);
                    f--;
                    sharedPreferences.edit().putInt("flag",f).apply();
                    sharedPreferences.edit().putStringSet("likedImages",set).apply();

                    swipeViewHolder.likeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
            }
        });

        swipeViewHolder.downloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "No permission!", Toast.LENGTH_SHORT).show();
                    // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
                }
                else {
                    Picasso.get().load(imgUrl).into(new Target() {
                        @Override
                        public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    long t = System.currentTimeMillis();
                                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + t + "temporary_file.jpg");
                                    if (file.exists()) file.delete();
                                    try {
                                        file.createNewFile();
                                        FileOutputStream ostream = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                                        ostream.flush();
                                        ostream.close();
                                        Log.d("Clickedbfr",""+save);
                                        save=true;
                                        Log.d("Clickedaft",""+save);
                                    } catch (Exception e) {
                                        Log.d("Working",e.getMessage()+",");
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                }
                if (save){
                    save=false;
                    Toast.makeText(mContext, "Successfully Downloaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeViewHolder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "No permission!", Toast.LENGTH_SHORT).show();
                    // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
                }
                else {
                    Picasso.get().load(imgUrl).into(new Target() {
                        @Override
                        public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    long t = System.currentTimeMillis();
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");
                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + t + "temporary_file.jpg");
                                    if (file.exists()) file.delete();
                                    try {
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
                                        if (file.createNewFile()) {
                                            FileOutputStream fo = new FileOutputStream(file);
                                            fo.write(bytes.toByteArray());
                                        }


                                    } catch (Exception e) {
                                        Log.d("Working",e.getMessage()+",");
                                        e.printStackTrace();
                                    }
                                    share.putExtra(Intent.EXTRA_TEXT,"Follow on Instagram! \n https://www.instagram.com/aravind048?r=nametag");
                                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + File.separator + t + "temporary_file.jpg"));
                                    mContext.startActivity(Intent.createChooser(share, "Share"));                                }
                            }).start();
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                }
            }
        });

      //  Log.d("positions",""+i);
        Intent intent = new Intent("message_subject_intent");
        intent.putExtra("QuoteImage" , imgUrl);
        intent.putExtra("postion",i);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);



       /* swipeViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("position",""+i);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return quotesImages.size();
    }
}
