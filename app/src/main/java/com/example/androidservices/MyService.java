package com.example.androidservices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.File;

public class MyService extends Service {
    NotificationManager mNotifyManager;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int ONGOING_NOTIFICATION_ID = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MyService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        // Constructing and delivering a notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                PRIMARY_CHANNEL_ID)
                .setContentIntent(contentPendingIntent)
                .setContentTitle("Notification")
                .setContentText("Sending a notification...")
                .setSmallIcon(R.drawable.ic_notif)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setTicker("Ticker");

        mNotifyManager.notify(0, builder.build());

        /*Intent notificationIntent = new Intent(this, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new Notification.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Notification")
                .setContentText("Sending a notification...")
                .setSmallIcon(R.drawable.ic_notif)
                .setContentIntent(pendingIntent)
                .setTicker("Ticker")
                .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
        raiseNotification(intent);*/

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
    private void raiseNotification(Intent inbound) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis());
        builder.setContentTitle("Notification")
                .setContentText("Sending a notification...")
                .setSmallIcon(R.drawable.ic_notif)
                .setTicker("Ticker");

        Intent outbound = new Intent(Intent.ACTION_VIEW);
        builder.setContentIntent(PendingIntent.getActivity(this, 0,
                outbound, 0));

        mNotifyManager.notify(ONGOING_NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Job Service Notification", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setDescription("Notification from Job Service");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
}
