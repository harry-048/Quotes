package com.rstream.biblequotes;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
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



    public SwipeQuoteAdapter(Activity mContext, ArrayList<String> quotesImages,ArrayList<String> quoteThumbImages, String motivationType, int imagePosition, DiscreteScrollView scrollView,int height, int width) {
        this.mContext = mContext;
        this.quotesImages = quotesImages;
        this.quoteThumbImages=quoteThumbImages;
        this.motivationType = motivationType;
        this.imagePosition = imagePosition;
        this.scrollView = scrollView;
        this.height= height;
        this.width=width;
        premiumDialogActivity=new PremiumDialogActivity(mContext);
        initializeBillingClient();

    }

    PremiumDialogActivity premiumDialogActivity;



    Activity mContext;
    ArrayList<String> quotesImages;
    ArrayList<String> quoteThumbImages;
    String motivationType;
    int imagePosition;
    String imgUrl;
    String thumbImgUrl;
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
    Bitmap bitback=null;
    Bitmap bitfront=null;

    private BillingClient billingClient;

    ProgressBar progressBar;
    Snackbar snackbar;


    @NonNull
    @Override
    public swipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(mContext).inflate(R.layout.swipe_content, viewGroup, false);
        final swipeViewHolder viewHolder = new swipeViewHolder(view);


        progressBar = view.findViewById(R.id.progressBar2);
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

        imgUrl=quotesImages.get(i);
        thumbImgUrl=quoteThumbImages.get(i);
        Log.d("selectedthumbnail",quoteThumbImages.get(i) + " , image: "+quotesImages.get(i));
       /* if (motivationType=="")
            imgUrl=quotesImages.get(i);
        else
            imgUrl=mContext.getString(R.string.imagelink)+motivationType+"/"+ quotesImages.get(i);*/

        swipeViewHolder.downloadImageView.setEnabled(false);
        swipeViewHolder.likeImageView.setEnabled(false);
        swipeViewHolder.wallpaperImageView.setEnabled(false);
        swipeViewHolder.shareImageView.setEnabled(false);

       Picasso.get().load(quoteThumbImages.get(i))
               .transform(new BlurTransformation(mContext))
               .into(swipeViewHolder.blurImageView);
        Log.d("showsnackbar"," before "+quotesImages.get(i)+"");
         snackbar = Snackbar.make(mContext.findViewById(android.R.id.content), "Image is loading please wait...", Snackbar.LENGTH_LONG);
        snackbar.show();
        Log.d("showsnackbar"," after "+quotesImages.get(i)+"");
        //Picasso.get().load(quotesImages.get(i)).into(swipeViewHolder.imageView);


        try {
            Log.d("imagecrashproblem",quotesImages.get(i)+"");
            Picasso.get().load(quotesImages.get(i)).noPlaceholder().into(swipeViewHolder.imageView, new Callback() {
                @Override
                public void onSuccess() {

                    swipeViewHolder.downloadImageView.setEnabled(true);
                    swipeViewHolder.likeImageView.setEnabled(true);
                    swipeViewHolder.wallpaperImageView.setEnabled(true);
                    swipeViewHolder.shareImageView.setEnabled(true);
                    if (snackbar.isShown()){
                        snackbar.dismiss();
                        Log.d("showsnackbar"," middle "+quotesImages.get(i)+"");
                    }
                    else
                        Log.d("showsnackbar","No snackbar "+quotesImages.get(i)+"");
                   /* if (snackbar.isShown())
                        Log.d("imagecrashproblem","snackbar "+quotesImages.get(i)+"");
                    else
                        Log.d("imagecrashproblem","No snackbar "+quotesImages.get(i)+"");*/
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(mContext, "Image failed loading!", Toast.LENGTH_SHORT).show();
                }

            });
        }
        catch (OutOfMemoryError ome){
            Toast.makeText(mContext, "Image failed loading!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "Image failed loading!", Toast.LENGTH_SHORT).show();
        }


        checkLike(swipeViewHolder);

        swipeViewHolder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLike(swipeViewHolder)){

                    if (likeCount>=2&& !purchased){
                        showPremiumDialog();
                    }
                    else {
                        set.add(quotesImages.get(i));
                        f++;
                        likeCount++;
                        sharedPreferences.edit().putInt("flag",f).apply();
                        sharedPreferences.edit().putStringSet("likedImages",set).apply();
                        sharedPreferences.edit().putInt("likeCount",likeCount).apply();


                        swipeViewHolder.likeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_red_24dp));

                    }

                }
                else {
                    set.remove(quotesImages.get(i));
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
                        Picasso.get().load(quotesImages.get(i)).into(new Target() {
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
                        URL url = new URL(quotesImages.get(i));
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
                        Uri fileUri = FileProvider.getUriForFile(mContext, "com.myfileprovider", cachePath);
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_TEXT, "Send From Bible Quotes");
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
                Log.d("imageurlclicked",imgUrl);
                Log.d("imageurlclicked","i="+i);
                Log.d("imageurlclicked",quotesImages.get(i)+"");
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "No permission!", Toast.LENGTH_SHORT).show();
                }
                else {
                    final Bitmap[] bitmapfront = new Bitmap[1];
                    final Bitmap[] bitmapback = new Bitmap[1];
                    Picasso.get().load(quotesImages.get(i))
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    Bitmap btm=null;
                                   /* long t = System.currentTimeMillis();
                                    File root = Environment.getExternalStorageDirectory();
                                    File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
*/
                                    long t = System.currentTimeMillis();
                                    final String path = Environment.getExternalStorageDirectory()+File.separator + t + "temporary_file.jpg";
                                    File file = new File(path);
                                    if (file.exists()) file.delete();
                                    try {
                                       /* cachePath.createNewFile();
                                        FileOutputStream ostream = new FileOutputStream(cachePath);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                        ostream.close();*/

                                        file.createNewFile();
                                        FileOutputStream ostream = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                                        ostream.flush();
                                        ostream.close();


                                    } catch (IOException e) {
                                        Log.d("heightofimagefffsss",",qwertyuixcvbn  ,  "+e.getMessage());
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    catch (Exception ee){
                                        Log.d("heightofimageesqwweesss",","+ee.getMessage());
                                    }
                                    float percent= ((width-bitmap.getWidth())*100)/width;

                                    int h= (int) ((bitmap.getHeight()*100)/(100-percent));

                                    btm= Bitmap.createScaledBitmap(bitmap, width, h, false);

                                    if (btm==null)
                                        Log.d("heightofimagefffs",",qazxcfg,   ");
                                    bitmapfront[0] =btm;
                                    bitfront=btm;
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });

                    Picasso.get().load(quoteThumbImages.get(i))
                            .transform(new BlurTransformation(mContext))
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    Bitmap btm=null;
                                   /* long t = System.currentTimeMillis();
                                    File root = Environment.getExternalStorageDirectory();
                                    File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
*/
                                    long t = System.currentTimeMillis();
                                    final String path = Environment.getExternalStorageDirectory()+File.separator + t + "temporary_file.jpg";
                                    File file = new File(path);
                                    if (file.exists()) file.delete();
                                    try {
                                       /* cachePath.createNewFile();
                                        FileOutputStream ostream = new FileOutputStream(cachePath);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                        ostream.close();*/

                                        file.createNewFile();
                                        FileOutputStream ostream = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                                        ostream.flush();
                                        ostream.close();




                                    } catch (IOException e) {
                                        Log.d("heightofimagees",","+e.getMessage());
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    catch (Exception ee){
                                        Log.d("heightofimageesqwwee",","+ee.getMessage());
                                    }
                                    btm= Bitmap.createScaledBitmap(bitmap, width, height, false);
                                    if (btm==null)
                                        Log.d("heightofimagees",",qwerty");
                                    bitmapback[0]=btm;
                                    bitback=btm;
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                    if (bitfront==null)
                        Log.d("heightofimage",",bvgshbghabsgs");
                    //Log.d("heightofimage",","+bitback.getHeight()+","+bitback.getConfig());
                    Bitmap bmOverlay = Bitmap.createBitmap(bitback.getWidth(), bitback.getHeight(), bitback.getConfig());
                    Canvas canvas = new Canvas(bmOverlay);
                    canvas.drawBitmap(bitmapback[0], new Matrix(), null);
                    int h=height/2-bitmapfront[0].getHeight()/2;
                    //Log.d("heightofimage",h+","+height+","+bitmapfront[0].getHeight());
                    canvas.drawBitmap(bitmapfront[0], 0, h, null);




              /*  Android.Webkit.MimeTypeMap mp = Android.Webkit.MimeTypeMap.Singleton;
                Intent intent = new Intent(Intent.ActionAttachData);
                intent.AddCategory(Intent.CategoryDefault);
                intent.SetDataAndType(path, mp.GetMimeTypeFromExtension("jpg"));
                intent.PutExtra("mimeType", mp.GetMimeTypeFromExtension("jpg"));
                intent.AddFlags(ActivityFlags.GrantReadUriPermission);
                this.StartActivityForResult(Intent.CreateChooser(intent,"Set as"),42);*/


                    try {
                        URL url = new URL(quotesImages.get(i));
                        View content = swipeViewHolder.imageView;
                        content.setDrawingCacheEnabled(true);
                        Bitmap bitmap = content.getDrawingCache();

                        File root = Environment.getExternalStorageDirectory();
                        File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                        // String cpath= root.getAbsolutePath() + "/DCIM/Camera/image.jpg";

                        long t = System.currentTimeMillis();
                        final String path = Environment.getExternalStorageDirectory()+File.separator + t + "temporary_file.jpg";
                        File file = new File(path);
                        if (file.exists()) file.delete();

                        try {

                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bmOverlay.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                            ostream.flush();
                            ostream.close();

                            MediaScannerConnection.scanFile(mContext, new String[]{file.toString()}, null,
                                    new MediaScannerConnection.OnScanCompletedListener() {
                                        public void onScanCompleted(String path, Uri uri) {
                                            galleryPath=uri+"";
                                            Log.d("errorwhenwallpaperqwer", galleryPath);
                                            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                            intent.setDataAndType(Uri.parse(galleryPath),"image/*");
                                            intent.putExtra("mimeType", "image/*");
                                            mContext.startActivity(Intent.createChooser(intent,"Set as"));

                                        }
                                    });

                         /*   cachePath.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(cachePath);
                            bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();*/
                        } catch (Exception e) {
                            Log.d("errorwhenwallpaperqwer",e.getMessage());
                            e.printStackTrace();
                        }





                    } catch(IOException e) {
                        Log.d("errorwhenwallpaper",e.getMessage());
                        e.printStackTrace();
                    }
                    catch (Exception e){
                        Log.d("errorwhenwallpapers",e.getMessage()+ " "+ galleryPath);
                        e.printStackTrace();
                    }

                }

            }
        });

        Intent intent = new Intent("message_subject_intent");
        intent.putExtra("QuoteImage" , quotesImages.get(i));
        intent.putExtra("postion",i);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


    }


    private void showPremiumDialog() {
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