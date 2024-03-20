package com.nassimlnd.flixhub.Service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nassimlnd.flixhub.R;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d("NotificationService", "From: " + message.getFrom());
        Log.d("NotificationService", "Message received" + message.getData());

        String title = message.getData().get("title");
        String messageBody = message.getData().get("message");

        NotificationChannel channel = new NotificationChannel("FlixHub", "FlixHub", NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification.Builder builder = new Notification.Builder(this, channel.getId())
                .setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        NotificationManagerCompat.from(this).notify(1, builder.build());

        super.onMessageReceived(message);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("NotificationService", "Refreshed token: " + token);
    }
}
