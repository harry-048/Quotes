package com.rstream.dailyquotes;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import jp.wasabeef.picasso.transformations.BlurTransformation;

import static android.content.Context.MODE_PRIVATE;

public class SwipeQuoteAdapter extends RecyclerView.Adapter<swipeViewHolder> implements PurchasesUpdatedListener {



    public SwipeQuoteAdapter(Activity mContext, ArrayList<String> quotesImages, String motivationType, int imagePosition, DiscreteScrollView scrollView,int height, int width) {
        this.mContext = mContext;
        this.quotesImages = quotesImages;
        this.motivationType = motivationType;
        this.imagePosition = imagePosition;
        this.scrollView = scrollView;
        this.height= height;
        this.width=width;
        premiumDialogActivity=new PremiumDialogActivity(mContext,this);
        initializeBillingClient();

    }

    PremiumDialogActivity premiumDialogActivity;



    Activity mContext;
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

    int imgPosition;
    String motivationName;
    int likeCount=0;
    int downloadCount=0;
    boolean purchased=false;
    int width=0;
    int height=0;
    WallpaperManager myWallpaperManager;

    private BillingClient billingClient;



    @NonNull
    @Override
    public swipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(mContext).inflate(R.layout.swipe_content, viewGroup, false);
        final swipeViewHolder viewHolder = new swipeViewHolder(view);



        sharedPreferences = mContext.getSharedPreferences("prefs.xml",MODE_PRIVATE);
        purchased = sharedPreferences.getBoolean("purchased",false);
        set = new HashSet<String>();
        set= sharedPreferences.getStringSet("likedImages",null);
        f= sharedPreferences.getInt("flag",0);
        imgPosition = sharedPreferences.getInt("imagePosition",0);
        motivationName = sharedPreferences.getString("motivationName",null);
        likeCount = sharedPreferences.getInt("likeCount",0);
        downloadCount = sharedPreferences.getInt("downloadCount",0);
        if (set==null){
            set = new HashSet<String>();
        }
        checkLike(viewHolder);

        //showImage(imagePosition,viewHolder);
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
        imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(imagePosition);
        Picasso.get().load(imgUrl).placeholder(mContext.getResources().getDrawable(R.drawable.loadinganimation)).into(viewHolder.imageView);

    }

    @Override
    public void onBindViewHolder(@NonNull final swipeViewHolder swipeViewHolder, final int i) {

        if (motivationType=="")
            imgUrl=quotesImages.get(i);
        else
            imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(i);

       Picasso.get().load(imgUrl)
               .transform(new BlurTransformation(mContext))
               .into(swipeViewHolder.blurImageView);

        Picasso.get().load(imgUrl).into(swipeViewHolder.imageView);


        checkLike(swipeViewHolder);

        swipeViewHolder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLike(swipeViewHolder)){

                    if (likeCount>=2&& !purchased){
                        showPremiumDialog();
                    }
                    else {
                        set.add(imgUrl);
                        f++;
                        likeCount++;
                        sharedPreferences.edit().putInt("flag",f).apply();
                        sharedPreferences.edit().putStringSet("likedImages",set).apply();
                        sharedPreferences.edit().putInt("likeCount",likeCount).apply();


                        swipeViewHolder.likeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_red_24dp));

                    }

                }
                else {
                    set.remove(imgUrl);
                    f--;
                    likeCount--;

                    sharedPreferences.edit().putInt("flag",f).apply();
                    sharedPreferences.edit().putStringSet("likedImages",set).apply();
                    sharedPreferences.edit().putInt("likeCount",likeCount).apply();

                    swipeViewHolder.likeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
            }
        });

        swipeViewHolder.downloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "No permission!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (downloadCount>=2&& !purchased){
                        showPremiumDialog();
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
                                            save=true;

                                            MediaScannerConnection.scanFile(mContext, new String[]{file.toString()}, null,
                                                    new MediaScannerConnection.OnScanCompletedListener() {
                                                        public void onScanCompleted(String path, Uri uri) {
                                                            galleryPath=uri+"";
                                                        }
                                                    });
                                            downloadCount++;
                                            sharedPreferences.edit().putInt("downloadCount",downloadCount).apply();
                                            Snackbar.make(mContext.findViewById(android.R.id.content), "Open Gallery", Snackbar.LENGTH_LONG)
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
                                            Log.d("errorwhensnackbar",e.getMessage());
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
                }

                if (save){
                    save=false;
                   // Toast.makeText(mContext, "Successfully Downloaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeViewHolder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "No permission!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        URL url = new URL(imgUrl);
                        View content = swipeViewHolder.imageView;
                        content.setDrawingCacheEnabled(true);
                        Bitmap bitmap = content.getDrawingCache();
                        long t = System.currentTimeMillis();
                        File root = Environment.getExternalStorageDirectory();
                        File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                        try {
                            cachePath.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(cachePath);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Uri fileUri = FileProvider.getUriForFile(mContext, "com.myfileprovider", cachePath);                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_TEXT, "Send From Daily Quotes");
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        share.setType("image/*");
                        share.putExtra(Intent.EXTRA_STREAM, fileUri);
                        mContext.startActivity(Intent.createChooser(share,"Share via"));
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        swipeViewHolder.wallpaperImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                myWallpaperManager= WallpaperManager.getInstance(mContext);
/*

                Picasso.get().load(imgUrl)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
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
                                          //  Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0 , width, height);
                                          //  Bitmap btm= Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                                            Log.d("errorwhensnackbars","imgwidth:"+bitmap.getWidth()+",imgheight:"+ bitmap.getHeight()+"width:"+width+"height:"+height);
                                            int value=0;
                                            float ratio;
                                            if (width>=height)
                                                ratio = width/height;
                                            else
                                                ratio=height/width;
                                            Log.d("errorwhensnackbarsd","gdbgdfbvsdgfb,"+width+","+height+","+height/width);
                                            int val;
                                            Bitmap btm=null;
                                            if (bitmap.getHeight() <= bitmap.getWidth()) {
                                                int bitratio = bitmap.getWidth()/bitmap.getHeight();

                                                Log.d("errorwhensnackbarsd",bitratio+"");
                                                value = bitmap.getHeight();
                                                val= (int) (width/2*ratio-bitmap.getWidth()/2);
                                                int w= (int) (bitmap.getWidth()*ratio);
                                                Log.d("errorwhensnackbarsd",w+",qwerty,"+val);
                                                btm = Bitmap.createBitmap(bitmap, val, 0, w, bitmap.getHeight());
                                                Log.d("errorwhensnackbarsd","asdfgh");
                                            } else {
                                                int bitratio = bitmap.getHeight()/bitmap.getWidth();
                                                Log.d("errorwhensnackbarsd",bitratio+"");
                                                value = bitmap.getWidth();
                                                btm = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2-bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getHeight()*bitratio);
                                            }

                                           // Bitmap btm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth()*ratio, bitmap.getHeight()*ratio);
                                           // Bitmap btm = resize(bitmap,width,height);
                                           // Bitmap btm = getResizedBitmap(bitmap, width, height);
                                          //  Log.d("errorwhensnackbarsd",btm.getHeight()/btm.getWidth()+"");
                                            Log.d("errorwhensnackbar","imgwidth:"+bitmap.getWidth()+",imgheight:"+ bitmap.getHeight()+"width:"+btm.getWidth()+"height:"+btm.getHeight());
                                           // myWallpaperManager.setWallpaperOffsetSteps(1, 1);
                                            myWallpaperManager.setBitmap(btm);



                                        } catch (Exception e) {
                                            Log.d("errorwhensnackba",e.getMessage());
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

*/

                try {
                    URL url = new URL(imgUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d("urlerrormessage",e.getMessage()+"");
                }
                View content = swipeViewHolder.imageView;
                content.setDrawingCacheEnabled(true);
                Bitmap bitmap = content.getDrawingCache();
                long t = System.currentTimeMillis();
                File root = Environment.getExternalStorageDirectory();
                File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");

                try {
                    cachePath.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(cachePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                    Log.d("errorwhensnackbar","imgwidth:"+bitmap.getWidth()+",imgheight:"+ bitmap.getHeight()+"width:"+width+"height:"+height);

                    Bitmap btm= Bitmap.createScaledBitmap(bitmap, width, height, false);

                    Log.d("errorwhensnackbars","imgwidth:"+bitmap.getWidth()+",imgheight:"+ bitmap.getHeight()+"width:"+btm.getWidth()+"height:"+btm.getHeight());

                    //Bitmap btm = getResizedBitmap(bitmap, height, width);

                  /*  myWallpaperManager.setBitmap(btm);
                    myWallpaperManager.setWallpaperOffsetSteps(.5f, 0.f);
                    myWallpaperManager.suggestDesiredDimensions(bitmap.getWidth(),bitmap.getHeight());
*/
                    myWallpaperManager.setBitmap(btm);
                    myWallpaperManager.setWallpaperOffsetSteps(.5f, 0.f);
                    myWallpaperManager.suggestDesiredDimensions(width,height);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        Intent intent = new Intent("message_subject_intent");
        intent.putExtra("QuoteImage" , imgUrl);
        intent.putExtra("postion",i);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
        //    image = Bitmap.createScaledBitmap(image, width/2-finalWidth/2, finalHeight, true);
            if (finalWidth>=finalHeight){
                int fWidth =  width/2-finalWidth/2;
                image= Bitmap.createBitmap(image, fWidth,0,finalWidth, finalHeight);
            }
            else {
                int fWidth =  width/2-finalWidth/2;
                image= Bitmap.createBitmap(image, fWidth,0,finalWidth, finalHeight);
            }
            return image;
        } else {
            return image;
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        /**
         *  create a matrix for the manipulation
         */

        Matrix matrix = new Matrix();

        /**
         *  resize the bit map
         */

        matrix.postScale(scaleWidth, scaleHeight);

        /**
         * recreate the new Bitmap
         */

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }

    private void showPremiumDialog() {
        /*new AlertDialog.Builder(mContext)
                .setMessage("Buy Premium For that Manhhh!!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        launchIAP();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();*/
            premiumDialogActivity.show();
    }

    private void initializeBillingClient(){
        billingClient = BillingClient.newBuilder(mContext).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    refreshPurchaseList();
                    premiumDialogActivity.setBillingClient(billingClient);
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    public void launchIAP() {
        if (billingClient.isReady()){
            List<String> skuList = new ArrayList<> ();
            skuList.add(mContext.getString(R.string.premium_sku));
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            if (responseCode == BillingClient.BillingResponse.OK
                                    && skuDetailsList != null) {
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
                                    if (mContext.getString(R.string.premium_sku).equals(sku)) {

                                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                .setSkuDetails(skuDetails)
                                                .build();
                                        int responseCode1 = billingClient.launchBillingFlow(mContext, flowParams);

                                    }
                                }

                            }
                        }
                    });
        }
    }

    private void refreshPurchaseList() {
        Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        List<Purchase> purchasedList = purchasesResult.getPurchasesList();
        for(Purchase purchase: purchasedList){
            if (purchase.getSku().equals(mContext.getString(R.string.premium_sku)))
            {
                purchased=true;
                sharedPreferences.edit().putBoolean("purchased",purchased).apply();
            }

        }
    }

    @Override
    public int getItemCount() {
        return quotesImages.size();
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        refreshPurchaseList();
    }
}
