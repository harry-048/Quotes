package com.rstream.teenwallpapers;

import android.content.Context;
import android.util.Log;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class ParseJsonData {

    Context mContext;
    public static ArrayList<String> images;
    public static ArrayList<String> thumbsImages;
    public static String motivationName = "";
    JSONObject data;

    public ParseJsonData(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<String> parseData() {
        InputStream in = null;
        if (mContext.getResources().getString(R.string.app_name).contains("Bible"))
            in = mContext.getResources().openRawResource(R.raw.bibledata);
        else if (mContext.getResources().getString(R.string.app_name).contains("Teen"))
            in = mContext.getResources().openRawResource(R.raw.teenwallpaper);
        else
            in = mContext.getResources().openRawResource(R.raw.data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
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
            images = new ArrayList();
            while (iter.hasNext()) {
                String key = iter.next();
                images.add(key.toUpperCase());
              /*  try {
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
                    motivationName = sb.toString();
                    Log.d("categoryin", key + "," + motivationName);
                    Log.d(key, data.get(key).toString());
                    Log.d("motivationinparse", motivationName);
                  //  motivationName = quoteskeyvalue.get(0).quoteKey;
                  //  putImagetoArray(key.toUpperCase());
                } catch (JSONException ee) {
                    ee.printStackTrace();
                }*/
            }

           // motivationName = quoteskeyvalue.get(0).quoteKey;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return images;
    }

    private void putImagetoArray(final String topicName) {
        final JSONArray[] imagesArray = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    imagesArray[0] = data.getJSONArray(topicName);
                    Log.d("imagesnamesarray",imagesArray[0].length()+"  ,  "+imagesArray[0]);
                    images = new ArrayList();
                    thumbsImages = new ArrayList();
                    for (int i = 0; i < imagesArray[0].length(); i++) {
                        Log.d("imagesnameslist",mContext.getString(R.string.imagelink)+topicName+"/thumbs/"+imagesArray[0].getString(i));
                        images.add(mContext.getString(R.string.imagelink)+topicName+"/"+imagesArray[0].getString(i));
                        thumbsImages.add(mContext.getString(R.string.imagelink)+topicName+"/thumbs/"+imagesArray[0].getString(i));

                        Log.d("imagesnames",images+"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).run();

    }


}
