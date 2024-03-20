package com.nassimlnd.flixhub.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d("NotificationService", "From: " + message.getFrom());
        Log.d("NotificationService", "Message received" + message.getData());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("NotificationService", "Refreshed token: " + token);
    }
}
