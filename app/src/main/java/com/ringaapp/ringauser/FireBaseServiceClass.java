package com.ringaapp.ringauser;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by andriod on 28/1/18.
 */

public class FireBaseServiceClass extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        Intent intent =new Intent(this,Categories.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int color = 0xff123456;
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notifica = new NotificationCompat.Builder(this);
        notifica.setContentTitle("Ringaapp ");
        notifica.setContentText(remoteMessage.getNotification().getBody());
        notifica.setAutoCancel(true);
        notifica.setColor(color);
        notifica.setOnlyAlertOnce(true);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notifica.setSound(alarmSound);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notifica.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notifica.setSmallIcon(R.mipmap.ic_launcher);
        }
        notifica.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifica.build());
    }


}