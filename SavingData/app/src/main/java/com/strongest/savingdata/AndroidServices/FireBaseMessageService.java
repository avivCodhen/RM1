package com.strongest.savingdata.AndroidServices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Activities.MyProgramsActivity;
import com.strongest.savingdata.Activities.ShareProgramActivity;
import com.strongest.savingdata.R;

import java.util.Map;

public class FireBaseMessageService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> payload = remoteMessage.getData();
            sendNotification(payload);
        }
    }

    private void sendNotification(Map<String, String> payload) {
        String userName = payload.get("title");
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.rm1_logo)
                .setContentTitle(userName + " sent you a program")
                .setContentText("check out "+payload.get("body"));

        Intent resultIntent = new Intent(this, HomeActivity.class);
        resultIntent.putExtra("userName", userName);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                HomeActivity.NOTIFICATION,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        int notificationId = (int)System.currentTimeMillis();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(notificationId, builder.build());

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("aviv", "onNewToken: " + s);
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(
                        getApplicationContext().getString(R.string.shared_preferences), MODE_PRIVATE
                );
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("token_id", s).commit();
    }

    public static String getToken(Context context){
        return context.getSharedPreferences(context.getString(R.string.shared_preferences), MODE_PRIVATE)
                .getString("token_id", "");
    }

}
