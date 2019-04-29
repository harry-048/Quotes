package com.rstream.dailyquotes;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

  String motivationName;
  JSONObject data;
  public static ArrayList<String> images;

  private void parseData() {
    InputStream in = getResources().openRawResource(R.raw.data);
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
      Iterator<String> iter = data.keys();
      while (iter.hasNext()) {
        String key = iter.next();

        try {
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
          Object value = data.get(key);
          putImagetoArray();
        } catch (JSONException ee) {
          // Something went wrong!
          ee.printStackTrace();
        }
      }


    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  private void putImagetoArray() {
    final JSONArray[] imagesArray = {null};
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          imagesArray[0] = data.getJSONArray(motivationName);
          images = new ArrayList();
          for (int i = 0; i < imagesArray[0].length(); i++) {
            images.add(imagesArray[0].getString(i));
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }).run();

  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    if(remoteMessage.getData()!=null)
       handleNow(remoteMessage);

    if (remoteMessage.getData().size() > 0) {
      Map<String, String> data = remoteMessage.getData();
      String myCustomKey = data.get("images");

      if (/* Check if data needs to be processed by long running job */ true) {
        // For long-running tasks (10 seconds or more) use WorkManager.
        // scheduleJob();
      } else {
        // Handle message within 10 seconds
        handleNow(remoteMessage);
      }
    }

    if (remoteMessage.getNotification() != null) {
      parseMessage(remoteMessage.getData().get("images"));
    }
  }

  private void handleNow(RemoteMessage remoteMessage) {
    Toast.makeText(this, "message, "+remoteMessage, Toast.LENGTH_SHORT).show();
  }

  private void parseMessage(String notificationMessage) {
    String message = notificationMessage;
    String s = message.replace(this.getString(R.string.imagelink),"");
    String[] str = s.split("/");
    motivationName = str[0];
    parseData();
    int i = images.indexOf(str[1]);
    Intent intent = new Intent(this,SwipeQuoteActivity.class);
    intent.putExtra("imageslist",images);
    intent.putExtra("clickedImage",i+"");
    intent.putExtra("Type",motivationName);
    intent.putExtra("className","MyFirebaseMessaging");
    startActivity(intent);
  }
}
