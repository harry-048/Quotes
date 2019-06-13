package com.rstream.dailyquotes;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
  int position=0;
  public static ArrayList<String> images;



  Target target = new Target() {
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
      sendNotification(bitmap);
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
  };

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
      Log.d("Errormessage", "message:"+e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      Log.d("ErrormessageIO", "message:"+e.getMessage());
      e.printStackTrace();
    } finally {
      try {
        in.close();
      } catch (IOException e) {
        Log.d("ErrormessageIOfinal", "message:"+e.getMessage());
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
          Log.d("Errormessagejson", "message:"+ee.getMessage());
          // Something went wrong!
          ee.printStackTrace();
        }
      }


    } catch (JSONException e) {
      Log.d("Errormessagejsonf", "message:"+e.getMessage());
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
    Log.d("Tokenmessage", "message:"+remoteMessage.getData() );

       //handleNow(remoteMessage);

    if (remoteMessage.getData().size() > 0) {
      Map<String, String> data = remoteMessage.getData();

     // String myCustomKey = data.get("images");
     // parseMessage(remoteMessage.getData().get("images"));

      if (/* Check if data needs to be processed by long running job */ true) {
        // For long-running tasks (10 seconds or more) use WorkManager.
        // scheduleJob();
      } else {
        // Handle message within 10 seconds
        //handleNow(remoteMessage);
      }
    }

    if (remoteMessage.getData()!= null){
      Log.d("Tokenmessageefefr", "message:");
      getImage(remoteMessage);
      //parseMessage(remoteMessage.getData().get("imageUrl"));
    }


    if (remoteMessage.getNotification() != null) {
     // Log.d("Tokenmessage", "haha message is here!"+remoteMessage.getData() );
     // getImage(remoteMessage);
     // parseMessage(remoteMessage.getData().get("images"));
    }
  }

  private void sendNotification(Bitmap bitmap) {
    NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
    style.bigPicture(bitmap);

    Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    Intent intent = new Intent(getApplicationContext(), SwipeQuoteActivity.class);

    intent.putExtra("imageslist",images);
    intent.putExtra("clickedImage",position+"");
    intent.putExtra("Type","happy");
    intent.putExtra("className","MyFirebaseMessaging");
    Log.d("Tokenmessage", "uahfaiuhfuiaewhofiuawe" );
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);



    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    String NOTIFICATION_CHANNEL_ID = "101";

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
      @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

      //Configure Notification Channel
      notificationChannel.setDescription("Game Notifications");
      notificationChannel.enableLights(true);
      notificationChannel.setVibrationPattern(new long[]{200});
      notificationChannel.enableVibration(false);

      notificationManager.createNotificationChannel(notificationChannel);
    }

    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(Config.title)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)
            .setStyle(style)
            .setWhen(System.currentTimeMillis())
            .setPriority(Notification.PRIORITY_MAX);


    notificationManager.notify(1, notificationBuilder.build());


  }

  private void getImage(final RemoteMessage remoteMessage) {

    Map<String, String> data = remoteMessage.getData();
    if (data.get("title")!=null && !data.get("title").trim().equals(""))
      Config.title = data.get("title");
    else
      Config.title = "Have a nice day";

    if (data.get("imageUrl")!=null && !data.get("imageUrl").trim().equals(""))
      Config.imageUrl = data.get("imageUrl");
    else
      Config.imageUrl = "http://riafyme.com/app/quotes/motivational/do-something-today-that-your-future-self-with-thank-you-for-quote-1.jpg";

   // Config.imageUrl = data.get("imageUrl");


       //Create thread to fetch image from notification
    if(remoteMessage.getData()!=null){
     // Log.d("Tokenmessageaaa", data.get("imageUrl")+ " ,message:, "+Config.imageUrl );
      images = new ArrayList();
      images.add(Config.imageUrl);
      position=0;
      Handler uiHandler = new Handler(Looper.getMainLooper());
      uiHandler.post(new Runnable() {
        @Override
        public void run() {
          // Get image from data Notification
          Picasso.get()
                  .load(Config.imageUrl)
                  .into(target);
        }
      }) ;
    }
  }

  private void handleNow(RemoteMessage remoteMessage) {

  }

  private void parseMessage(String notificationMessage) {
    Log.d("Tokenmessagesss", "message:"+notificationMessage );
    String message = notificationMessage;
    String s = message.replace(this.getString(R.string.imagelink),"");
    String[] str = s.split("/");
    motivationName = str[0];
    parseData();
   // int i = images.indexOf(str[1]);
    Log.d("Tokenmessagesssddd", "message length"+str.length+", "+str[0]+" , "+str[1] );
    position=images.indexOf(str[1]);
   /* Intent intent = new Intent(this,SwipeQuoteActivity.class);
    intent.putExtra("imageslist",images);
    intent.putExtra("clickedImage",i+"");
    intent.putExtra("Type",motivationName);
    intent.putExtra("className","MyFirebaseMessaging");
    startActivity(intent);*/
  }
}
