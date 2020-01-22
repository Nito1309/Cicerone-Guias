package com.nitoelchidoceti.ciceroneguias.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nitoelchidoceti.ciceroneguias.LoginActivity;
import com.nitoelchidoceti.ciceroneguias.R;

public class MessagingService extends FirebaseMessagingService {
    public static final String TAG = "NOTICIAS";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "GUIA NUEVA NOTIFICACION");

        String from = remoteMessage.getFrom();
        Log.d(TAG, "GUIA Mensaje recibido de : " + from);
        if (remoteMessage.getNotification() != null){
            Log.d(TAG, "GUIA Notificación: " + remoteMessage.getNotification().getBody());
            mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0){
            Log.d(TAG,"GUIA Data: " + remoteMessage.getData());
        }
    }

    private void mostrarNotificacion(String title, String body) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 ,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "MyChannel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.colorPrimary);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                "MyChannel")
                .setSmallIcon(R.drawable.ic_guides)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(body)
                .setSound(sound)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "GUIA Token: " + s);
    }
}
