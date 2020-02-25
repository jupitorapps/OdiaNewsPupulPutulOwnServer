package com.pupulputulapps.oriyanewspaper.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pupulputulapps.oriyanewspaper.MainActivity;
import com.pupulputulapps.oriyanewspaper.R;
import java.util.Map;
import java.util.Random;

public class FireMsgService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        if (data == null) return;

        String body = data.get("message");
        String title = data.get("title");
        String clickaction = data.get("click_action");

        Intent intent;
        try {
            if (clickaction.equals("com.pupulputulapps.oriyanewspaper.NewsAdvancedWebViewActivity")){
                intent = new Intent(this, MainActivity.class);
                String id = data.get("id");
                intent.putExtra("id", id);
                intent.putExtra("from", "notification");
            }else{
                intent= new Intent(this, MainActivity.class);

            }
        } catch (Exception e) {
            e.printStackTrace();
            intent = new Intent(this, MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1210,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle(title);
        style.bigText(body);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channel_id = getString(R.string.default_notification_channel_id); // default_channel_id
            String channel_title = getString(R.string.default_notification_channel_title); // Default Channel

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(channel_id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channel_id, channel_title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }

            notificationBuilder = new
                    NotificationCompat.Builder(this, channel_id)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setStyle(style);
        }else{
            notificationBuilder = new
                    NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setStyle(style);
        }

        int notiID = 112;
        try{
            Random rand = new Random();
            notiID = rand.nextInt(100) + 1;
        }catch (Exception e){
            notiID = 111;
        }

        notificationManager.notify(notiID, notificationBuilder.build());


    }
}