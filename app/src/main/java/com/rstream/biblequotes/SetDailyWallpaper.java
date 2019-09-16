package com.rstream.biblequotes;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import jp.wasabeef.picasso.transformations.BlurTransformation;

import static android.content.Context.MODE_PRIVATE;

public class SetDailyWallpaper extends BroadcastReceiver {

SharedPreferences sharedPreferences;
    public SetDailyWallpaper() {

    }
    String motivationType;
    int daycount =0;
    String imageName = "";
    String thumbImageName = "";
    Bitmap bitback=null;
    Bitmap bitfront=null;
    int width = 0;
    int height = 0;
    @Override
    public void onReceive(Context context, Intent intent) {


      /*  try {
            width = intent.getExtras().getInt("screenwidth");
            height = intent.getExtras().getInt("screenheight");
            Log.d("calendartime","width is "+ width + " , height is "+height);
        }
        catch (Exception e){
            Log.d("calendartime","error is "+ e);
        }*/

       // intentTypeName = context.getIntent().getStringExtra("className");
      //  Log.d("calendartime","from the class ");


        sharedPreferences = context.getSharedPreferences("prefs.xml",MODE_PRIVATE);


        width = sharedPreferences.getInt("screenWidth",1);
        height = sharedPreferences.getInt("screenHeight",1);

        daycount = sharedPreferences.getInt("daycount",1);
        motivationType=sharedPreferences.getString("notificationType","");
      //  Log.d("calendartime","from the class  , "+sharedPreferences.getBoolean("setDailyWallpaper",false));
        if (sharedPreferences.getBoolean("setDailyWallpaper",false)){
        //    Log.d("calendartime","walked  ");
            //setWallpapet(context);
            parseData(context);
        }


    }




    JSONObject data;

    private void parseData(Context context) {
        InputStream in = null;
        if (context.getResources().getString(R.string.app_name).contains("Bible"))
            in = context.getResources().openRawResource(R.raw.bibledata);
        else if (context.getResources().getString(R.string.app_name).contains("Teen"))
            in = context.getResources().openRawResource(R.raw.teenwallpaper);
        else
            in = context.getResources().openRawResource(R.raw.data);
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
                //Log.d("calendartime", " key is "+ key + " , "+ motivationType);
                try {
                    /*String[] words = key.split(" ");
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
                    motivationName = quoteskeyvalue.get(0).quoteKey;*/
                    if (key.equals(motivationType))
                        putImagetoArray(context);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    ArrayList<String> images;
    ArrayList<String> thumbsImages;
    private void putImagetoArray(final Context context) {
        final JSONArray[] imagesArray = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                //    Log.d("calendartime", " key is "+ motivationType + " day "+daycount);

                    imagesArray[0] = data.getJSONArray(motivationType);
                   // ArrayList<String> images = new ArrayList();
                    //ArrayList<String> thumbsImages= new ArrayList();



                 //   Log.d("calendartime", " key is "+  imagesArray[0].length()+ " ,before  "+ motivationType);
                    images = new ArrayList();
                    thumbsImages = new ArrayList();
                  //  Log.d("calendartime", " key is "+  imagesArray[0].length()+ " , "+ motivationType);
                    if (daycount<imagesArray[0].length()){
                        imageName = context.getString(R.string.imagelink)+motivationType+"/"+imagesArray[0].getString(daycount);
                        thumbImageName = context.getString(R.string.imagelink)+motivationType+"/thumbs/"+imagesArray[0].getString(daycount);
                        sharedPreferences.edit().putInt("daycount",daycount+1).apply();
                    }
                    else {
                        imageName = context.getString(R.string.imagelink)+motivationType+"/"+imagesArray[0].getString(0);
                        thumbImageName = context.getString(R.string.imagelink)+motivationType+"/thumbs/"+imagesArray[0].getString(0);
                        sharedPreferences.edit().putInt("daycount",1).apply();
                    }

                    try {
                        setWallpaper(context);
                    }
                    catch (Exception e){
                        Log.d("calendartime", " setwallpaper error is "+ e+ " ,ee ");
                    }
                   /* for (int i = 0; i < imagesArray[0].length(); i++) {
                        images.add(context.getString(R.string.imagelink)+motivationType+"/"+imagesArray[0].getString(i));
                        thumbsImages.add(context.getString(R.string.imagelink)+motivationType+"/thumbs/"+imagesArray[0].getString(i));
                        //images.add(imagesArray[0].getString(i));
                    }*/
                } catch (JSONException e) {
                    Log.d("calendartime", " error"+ e+ " ,e ");
                    e.printStackTrace();
                }
                catch (Exception ee){
                    Log.d("calendartime", " error"+ ee+ " ,ee ");
                    ee.printStackTrace();
                }
            }
        }).run();

    }


    private void setWallpaper(final Context mContext){


        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "No permission!", Toast.LENGTH_SHORT).show();
        }
        else {
          //  Log.d("calendartime","entered  ");
            final Bitmap[] bitmapfront = new Bitmap[1];
            final Bitmap[] bitmapback = new Bitmap[1];
            Picasso.get().load(imageName)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                          //  Log.d("calendartime","first bitmap loaded  ");
                            Bitmap btm=null;

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


                            } catch (IOException e) {
                                Log.d("heightofimagefffsss",",qwertyuixcvbn  ,  "+e.getMessage());
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            catch (Exception ee){
                                Log.d("heightofimageesqwweesss",","+ee.getMessage());
                            }
                           // float percent= ((width-bitmap.getWidth())*100)/width;
                            float percent= ((width-bitmap.getWidth())*100);
                            percent = percent/width;

                            int h= (int) ((bitmap.getHeight()*100)/(100-percent));

                            btm= Bitmap.createScaledBitmap(bitmap, width, h, false);

                            if (btm==null)
                                Log.d("heightofimagefffs",",qazxcfg,   ");
                            bitmapfront[0] =btm;
                            bitfront=btm;





                            Picasso.get().load(thumbImageName)
                                    .transform(new BlurTransformation(mContext))
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    //        Log.d("calendartime","second bitmap loaded  ");
                                            Bitmap btm=null;

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

                                          //  Log.d("calendartime","before entering  ");
                                            if (bitmapfront[0]!=null && bitmapback[0]!=null){
                                              //  Log.d("getwidthofbitmap","before");
                                                Bitmap bmOverlay = Bitmap.createBitmap(bitback.getWidth(), bitback.getHeight(), bitback.getConfig());
                                              //  Log.d("getwidthofbitmap","after");
                                                Canvas canvas = new Canvas(bmOverlay);
                                                canvas.drawBitmap(bitmapback[0], new Matrix(), null);
                                                int h=height/2-bitmapfront[0].getHeight()/2;

                                                canvas.drawBitmap(bitmapfront[0], 0, h, null);

                                                WallpaperManager wallpaperManager =
                                                        WallpaperManager.getInstance(mContext);
                                                try {
                                                    wallpaperManager.setBitmap(bmOverlay);
                                                } catch (IOException e) {
                                                    Log.d("wallpaperexception","error is "+e);
                                                    e.printStackTrace();
                                                }
                                                catch (Exception ee){
                                                    Log.d("wallpaperexception","error is "+ee);
                                                    ee.printStackTrace();
                                                }
                                            }
                                            else {
                                                Log.d("calendartime", "data is null");
                                            }


                                        }

                                        @Override
                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                            Log.d("calendartime","second bitmap error  "+e);
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                            Log.d("calendartime","second bitmap loading  ");
                                        }
                                    });

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


}
