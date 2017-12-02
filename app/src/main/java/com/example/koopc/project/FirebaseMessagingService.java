package com.example.koopc.project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;

//

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final String TAG = "FirebaseMsgService";
    public static String nowBuilding = ""; // 현재 어느 빌딩에 있는지를 확인
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getNotification().getTitle() == null){ // 파베에서 타이틀을 빌딩 이름으로 전달하고
            sendNotification(remoteMessage.getNotification().getBody()); // 없으면 그냥 어플을 가진 모든 애들에게 푸시 전달
        }else if(remoteMessage.getNotification().getTitle().equals(nowBuilding)) { // 빌딩이름을 설정하여 전달하면
            sendNotification(remoteMessage.getNotification().getBody()); // 해당 빌딩에 있는 사람들만 푸시 받음.
        }

    }

    private void sendNotification(String messageBody){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // 푸시 아이콘
                .setContentTitle("Notice!") // 푸시에 뜨는 제목
                .setContentText(messageBody) // 전달한 메세지
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    public FirebaseMessagingService() {

    }
}