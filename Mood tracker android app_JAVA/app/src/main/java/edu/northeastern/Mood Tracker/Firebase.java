package edu.northeastern.mobileapplicationteam18;

import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Firebase extends FirebaseMessagingService {
    private String userName;
    private String userId;
    private DatabaseReference userDB;
    private static final Map<Integer, Integer> emojiDrawable = new HashMap<>();
    //private static final String TAG = "mFirebaseIIDService";
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";


    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();
        userDB = FirebaseDatabase.getInstance().getReference();
        userId = Settings.Secure.getString(
                getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        userDB.child("users").child(userId).get().addOnCompleteListener(task -> {
            HashMap userMap = (HashMap) task.getResult().getValue();
            if (userMap == null) {
                return;
            }
            userName = Objects.requireNonNull(userMap.get("username")).toString();
        });
//            if (task.isSuccessful()) {
//                HashMap userMap = (HashMap) task.getResult().getValue();
//                if (userMap != null)  {
//                    userName = Objects.requireNonNull(userMap.get("username")).toString();
//                }
//                return;
//            }
//
//        });
        emojiDrawable.put(1, R.drawable.angry);
        emojiDrawable.put(2, R.drawable.confused);
        emojiDrawable.put(3, R.drawable.heartbreak);
        emojiDrawable.put(4, R.drawable.sad);
        emojiDrawable.put(5, R.drawable.sleepy);
        emojiDrawable.put(6, R.drawable.naughty);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        @SuppressLint("HardwareIds") User user = new User(this.userName, Settings.Secure.getString(
                getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID), token);
        userDB.child("users").child(user.userDeviceID).setValue(user);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage emojiMessage) {
        myClassifier(emojiMessage);
    }

    private void myClassifier(RemoteMessage emojiMessage) {
        String identifier = emojiMessage.getFrom();
        if (identifier != null) {
            if (identifier.contains("topic")) {
                if (emojiMessage.getNotification() != null) {
                    showNotification(emojiMessage.getNotification());
                }
            } else {
                if (emojiMessage.getData().size() > 0) {
                    RemoteMessage.Notification notification = emojiMessage.getNotification();
                    assert notification != null;
                    showNotification(notification, Integer.parseInt(Objects.requireNonNull(emojiMessage.getData().get("image_id"))));
                }
            }
        }
    }
//cited from `https://developer.android.com/develop/ui/views/notifications/build-notification`
//cited from `https://developer.android.com/develop/ui/views/notifications/navigation`
    private void showNotification(RemoteMessage.Notification emojiNotification, int emojiId) {
        Intent intent = new Intent(this, SentEmojiActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationManager.createNotificationChannel(notificationChannel);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Bitmap bp = BitmapFactory.decodeResource(getResources(), emojiDrawable.get(emojiId));
        notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(emojiNotification.getTitle())
                .setContentText(emojiNotification.getBody())
                .setLargeIcon(bp)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bp)
                        .bigLargeIcon(null))
                .addAction(R.mipmap.ic_launcher, "Snooze", pendingIntent)
                .build();
        notificationManager.notify(0, notification);

    }

    private void showNotification(RemoteMessage.Notification emojiNotification) {
        Intent intent = new Intent(this, SentEmojiActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationManager.createNotificationChannel(notificationChannel);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(emojiNotification.getTitle())
                .setContentText(emojiNotification.getBody())
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(0, notification);
    }
}
