package com.example.lego;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.lego.GoogleMaps;
import com.example.lego.R;


import androidx.core.app.NotificationCompat;

public class DelayedMessageService {

        private static final String CHANNEL_ID = "channel_id";
        private static final String CHANNEL_NAME = "Channel Name";
        private static final int NOTIFICATION_ID = 1;

        public static void createNotification(Context context, String title, String message) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(context, GoogleMaps.class);
            PendingIntent pendingIntent;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.button_rounded)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.button_rounded))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

