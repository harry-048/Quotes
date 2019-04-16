package com.rstream.dailyquotes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.rstream.dailyquotes.R;
import com.jgabrielfreitas.core.BlurImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import jp.wasabeef.picasso.transformations.BlurTransformation;

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
    View view;
    String galleryPath;
    BlurImageView blurbackImageView;
    private BillingClient billingClient;
    int imgPosition;
    String motivationName;

    @NonNull
    @Override
    public swipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(mContext).inflate(R.layout.swipe_content, viewGroup, false);
        final swipeViewHolder viewHolder = new swipeViewHolder(view);



        sharedPreferences = mContext.getSharedPreferences("prefs.xml",MODE_PRIVATE);
        set = new HashSet<String>();
        set= sharedPreferences.getStringSet("likedImages",null);
        f= sharedPreferences.getInt("flag",0);
        imgPosition = sharedPreferences.getInt("imagePosition",0);
        motivationName = sharedPreferences.getString("motivationName",null);
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
        Picasso.get().load(imgUrl).placeholder(mContext.getResources().getDrawable(R.drawable.loadinganimation)).into(viewHolder.imageView);

    }

    @Override
    public void onBindViewHolder(@NonNull final swipeViewHolder swipeViewHolder, final int i) {

       // showImage(i,swipeViewHolder);
        imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(i);
       // showImage(i,swipeViewHolder);
       /* Picasso.get().load(imgUrl).into(swipeViewHolder.blurImageView);
        swipeViewHolder.blurImageView.setBlur(15);*/

       /* swipeViewHolder.blurImageView.setBlurImageByUrl(imgUrl);
        swipeViewHolder.blurImageView.setBlurFactor(8);
        swipeViewHolder.blurImageView.setFailDrawable(mContext.getResources().getDrawable(R.drawable.loadinganimation));
        swipeViewHolder.blurImageView.setDefaultDrawable(mContext.getResources().getDrawable(R.drawable.loadinganimation));*/

       Picasso.get().load(imgUrl)
               .transform(new BlurTransformation(mContext))
               .into(swipeViewHolder.blurImageView);
        //swipeViewHolder.blurImageView.getResources().getDrawable(R.drawable.ic_favorite_red_24dp);
        Picasso.get().load(imgUrl).into(swipeViewHolder.imageView);

       /* try {
            Blurry.with(mContext).capture(swipeViewHolder.blurImageView).into(swipeViewHolder.blurImageView);
        }
        catch (NullPointerException e){
            Log.d("Blurerror",e.getMessage());
        }*/

        Log.d("Blurwork","yes");

        checkLike(swipeViewHolder);

        swipeViewHolder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLike(swipeViewHolder)){

                    set.add(imgUrl);
                    f++;
                    sharedPreferences.edit().putInt("flag",f).apply();
                    sharedPreferences.edit().putStringSet("likedImages",set).apply();
                    sharedPreferences.edit().putInt("imagePosition",i).apply();
                    sharedPreferences.edit().putString("motivationName",motivationType).apply();
                    //likedImages.add(imgUrl);
                    swipeViewHolder.likeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
                }
                else {
                    set.remove(imgUrl);
                    f--;

                    sharedPreferences.edit().putInt("flag",f).apply();
                    sharedPreferences.edit().putStringSet("likedImages",set).apply();
                    sharedPreferences.edit().putInt("imagePosition",i).apply();
                    sharedPreferences.edit().putString("motivationName",motivationType).apply();
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
                                    final String path = Environment.getExternalStorageDirectory()+File.separator + t + "temporary_file.jpg";
                                    File file = new File(path);
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

                                        MediaScannerConnection.scanFile(mContext, new String[]{file.toString()}, null,
                                                new MediaScannerConnection.OnScanCompletedListener() {
                                                    public void onScanCompleted(String path, Uri uri) {
                                                        Log.d("ExternalStorage", "Scanned " + path + ":");
                                                        Log.d("ExternalStorage", "-> uri=" + uri);
                                                        galleryPath=uri+"";
                                                    }
                                                });
                                        Snackbar.make(view, "Open Gallery", Snackbar.LENGTH_LONG)
                                                .setAction("View", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                     //   Toast.makeText(mContext, "snackbar", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Intent.ACTION_PICK,
                                                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                        intent.setAction(android.content.Intent.ACTION_VIEW);
                                                        intent.setDataAndType(Uri.parse(galleryPath),"image/*");
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        mContext.startActivity(intent);
                                                    }
                                                }).show();
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
                                    Intent share = new Intent();
                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    String path = Environment.getExternalStorageDirectory()+File.separator + t + "temporary_file.jpg";
                                    //File root = Environment.getExternalStorageDirectory();
                                   // File cachePath = new File(root.getAbsolutePath() + File.separator + t + "temporary_file.jpg");
                                    File file = new File(path);
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
                               //     Uri contentUri = FileProvider.getUriForFile(this, Environment.getExternalStorageDirectory() + File.separator + t + "temporary_file.jpg");
                                    Log.d("imageshare",""+Uri.parse(Environment.getExternalStorageDirectory() + File.separator + t + "temporary_file.jpg"));
                                    share.setAction(Intent.ACTION_SEND);
                                    share.setType("image/*");
                                    share.putExtra(Intent.EXTRA_TEXT,"Send From Quotes");
                                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                                    mContext.startActivity(Intent.createChooser(share, "Share"));
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
