package com.rstream.dailyquotes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v7.app.AlertDialog;
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
import com.rstream.dailyquotes.R;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import jp.wasabeef.picasso.transformations.BlurTransformation;

import static android.content.Context.MODE_PRIVATE;

public class SwipeQuoteAdapter extends RecyclerView.Adapter<swipeViewHolder> implements PurchasesUpdatedListener {



    public SwipeQuoteAdapter(Activity mContext, ArrayList<String> quotesImages, String motivationType, int imagePosition, DiscreteScrollView scrollView) {
        this.mContext = mContext;
        this.quotesImages = quotesImages;
        this.motivationType = motivationType;
        this.imagePosition = imagePosition;
        this.scrollView = scrollView;
        Log.d("displayimagess",motivationType+","+imagePosition);
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
        Log.d("position",imagePosition+"b");
        imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(imagePosition);
        Picasso.get().load(imgUrl).placeholder(mContext.getResources().getDrawable(R.drawable.loadinganimation)).into(viewHolder.imageView);

    }

    @Override
    public void onBindViewHolder(@NonNull final swipeViewHolder swipeViewHolder, final int i) {

        if (motivationType=="")
            imgUrl=quotesImages.get(i);
        else
            imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(i);
        Log.d("displayimage",motivationType+","+mContext.getString(R.string.imagelink));
        Log.d("displayimagesss",imgUrl+", "+i);
        Log.d("displayimagessss",motivationType+"/"+ quotesImages.get(i));
       Picasso.get().load(imgUrl)
               .transform(new BlurTransformation(mContext))
               .into(swipeViewHolder.blurImageView);
        //swipeViewHolder.blurImageView.getResources().getDrawable(R.drawable.ic_favorite_red_24dp);
        Picasso.get().load(imgUrl).into(swipeViewHolder.imageView);

        Log.d("Blurwork","yes");

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

                        //likedImages.add(imgUrl);
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
                    // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
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
                                            downloadCount++;
                                            Log.d("downloadCount",downloadCount+"");
                                            sharedPreferences.edit().putInt("downloadCount",downloadCount).apply();
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
                    try {
                        URL url = new URL(imgUrl);
                       // Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        View content = swipeViewHolder.imageView;
                        content.setDrawingCacheEnabled(true);
                        Bitmap bitmap = content.getDrawingCache();
                        long t = System.currentTimeMillis();
                        File root = Environment.getExternalStorageDirectory();
                        File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                        //File cachePath = new File(root.getAbsolutePath() + File.separator + t + "temporary_file.jpg");
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
                        Log.d("sharingnotworking","true"+e.getMessage());
                        System.out.println(e);
                    }
                    catch (Exception e){
                        Log.d("sharingnotworking","right"+e.getMessage());
                    }
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
                                        //Toast.makeText(mContext, price, Toast.LENGTH_SHORT).show();
                                        Log.d("PremiumUpgrade", price);

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
            //Toast.makeText(this, "Purchased " + purchase.getSku(), Toast.LENGTH_SHORT).show();
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
