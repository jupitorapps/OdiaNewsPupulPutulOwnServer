package com.pupulputulapps.oriyanewspaper.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pupulputulapps.oriyanewspaper.MainActivity;
import com.pupulputulapps.oriyanewspaper.NewsAdvancedWebViewActivity;
import com.pupulputulapps.oriyanewspaper.R;
import com.pupulputulapps.oriyanewspaper.VideoPlayerActivity;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class FireMsgService extends FirebaseMessagingService {
    private Bitmap bigPicture;
    private static final String TAG = "FireMsgService TAGG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        if (data == null || data.isEmpty()) return;

        String body = data.get("message");
        String title = data.get("title");
        String news_link = data.get("news_link");

        String image_link = data.get("image_link");
        String video_thumbnail = data.get("https://i.ytimg.com/vi/"+"yt_video_id"+"/0.jpg");

        String clickaction = data.get("click_action");
        Log.d(TAG, "onMessageReceived: title: " + title + " news_link: " + news_link + " Image link: " + image_link);



        Intent intent;
        try {
            if (Objects.requireNonNull(clickaction).equals("com.pupulputulapps.oriyanewspaper.NewsAdvancedWebViewActivity")) {
                intent = new Intent(this, NewsAdvancedWebViewActivity.class);
                String id = data.get("id");
                intent.putExtra("id", id);
                intent.putExtra("from", "notification");


                intent.putExtra("url", news_link);
                intent.putExtra("paper_name", title);
                intent.putExtra("news_source_id",id);
                intent.putExtra("source","Notification");

                try {
                    URL url = new URL(image_link);
                    bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    // System.out.println(e.toString());
                }


            } else {
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("from","notification");

                try {
                    URL url = new URL(video_thumbnail);
                    bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    // System.out.println(e.toString());
                }
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
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bigPicture)
                            .bigLargeIcon(null))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent);
            // .setStyle(style);
        } else {
            notificationBuilder = new
                    NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bigPicture)
                            .bigLargeIcon(null))
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent);
            //  .setStyle(style);
        }

        int notiID = 112;
        try {
            Random rand = new Random();
            notiID = rand.nextInt(100) + 1;
        } catch (Exception e) {
            notiID = 111;
        }

        notificationManager.notify(notiID, notificationBuilder.build());
    }


}