package com.rstream.dailyquotes;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
       // FirebaseMessaging.getInstance().subscribeToTopic("all");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("com.rstream.dailyquotes");
        Log.d("Tokenmessagedsads", "Refreshed token: " + refreshedToken);

    }
}
