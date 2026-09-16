package com.example.healthcareproject.ui.firebasenotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.healthcareproject.R;
import com.example.healthcareproject.ui.patients.PatientsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM_TELEMED";
    private static final String CHANNEL_ID = "telemed_health_alerts";
    private static final String CHANNEL_NAME = "Telemed Healthcare Alerts";

    @Override
    public void onNewToken(@NonNull String token) { //Every new device which install the app can receive its own token after enbaling the firebase.
        super.onNewToken(token);
        Log.d(TAG, "FCM Registration token generated: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) { //It handles the incoming firebase messages or notifications.
        super.onMessageReceived(message);
        Log.d(TAG, "Message payload: "+ message); //Helps to inspect what firebase sent.

        if(message.getNotification() != null){ //For checking that does the firebase message contains any notification payloads. If yes
            String title = message.getNotification().getTitle(); //For getting the title of the message/notification
            String body = message.getNotification().getBody(); //For getting the body

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Crucial Alerts");
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                if(manager != null){
                    manager.createNotificationChannel(channel);
                }
            }

            Intent intent = new Intent(this, PatientsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.caduceus_logo_red)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(Color.RED)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            int notificationId = (int) System.currentTimeMillis();
            if(manager != null){
                manager.notify(notificationId, builder.build());
            }
        }
    }

//    private void triggerLocalNotification(String title, String body){ // It takes the title and body and showing them in the phone as a notification.
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); //Creating the notification manager as the androids notification controller it tells the android to show the notification.
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //Checking whether the app is running in the android version 8 or newer one.
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH); //Creating a notification category called Telemed Healthcare Alerts with high importance.
//            channel.setDescription("Crucial emergency medial alerts and patient updates channel.");
//            if(notificationManager != null){ //Checking if the Notication manager is showing any notification or not
//                notificationManager.createNotificationChannel(channel); //It means the registering the notification channel in the android.
//            }
//        }
//
//        int notificationId = (int) System.currentTimeMillis(); //Every notification has its own notification ID, we are registering here everything.
//
//        Intent intent = new Intent(this, patients.class); //When a user taps the notification it will directly go into the patients' activity.
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//The flags tell android how to open then patients' activity.
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); //Pending Intent is does a task later. Where Intent tells which activity has to open next then the pending intent is keeps that activity ready for later.
//
//        //Notification Compat is a helper for notifications.
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID) //Creating a notification builder to store the ICON, TITLE, MESSAGE, CLICK ACTION.
//                .setSmallIcon(R.drawable.caduceus_logo_red) //An ICON displayed in the notification
//                .setColor(ContextCompat.getColor(this, R.color.c_red))
//                .setContentTitle(title)//Title of the notification
//                .setContentText(body)//Body of the notification
//                .setAutoCancel(true)//when the user taps on the notification it automatically removes or display it.
//                .setPriority(NotificationCompat.PRIORITY_HIGH) //Setting the notification as HIGH priority notification.
//                .setContentIntent(pendingIntent); //Setting the pending intent for opening the patients' activity.
//        if(notificationManager != null){
//            notificationManager.notify(notificationId, builder.build()); //Combining all of the above steps and create the notification.
//        }
//    }
}
