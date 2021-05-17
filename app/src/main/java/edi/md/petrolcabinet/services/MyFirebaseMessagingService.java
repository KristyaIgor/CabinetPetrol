package edi.md.petrolcabinet.services;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import edi.md.petrolcabinet.MainActivityV1;
import edi.md.petrolcabinet.NotificationListActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.realm.objects.Company;
import edi.md.petrolcabinet.realm.objects.NotificationRealm;
import io.realm.Realm;

/**
 * Created by Igor on 19.08.2020
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG", "onMessageReceived: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();

            sendNotification(remoteMessage.getData());
        }
    }
    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }
    private void sendRegistrationToServer(String token) {
        getSharedPreferences("firebase",MODE_PRIVATE).edit().putString("token",token).apply();
    }

    private void sendNotification(Map<String, String> data) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIconBMP = null;
        Bitmap companyLogo = null;
        String companyName = null;

        String clientID = data.get("clientID");
        String category = data.get("Category");
        String messageBody = data.get("body");
        String messageTitle = data.get("title");
        String companyId = data.get("companyID");

        int dataType = 0;
        if(category != null)
            dataType = Integer.parseInt(category);

        String notificationChanel = "Default notification channel";
        String shortAction = "";
        switch (dataType){
            case 0:{
                notificationChanel = "Personal notification";
                shortAction = getString(R.string.short_title_personal_notification);
            }break;
            case 1:{
                notificationChanel = "News notification"; //news
                shortAction = getString(R.string.short_title_news_notification);
            }break;
            case 2:{
                notificationChanel = "New discount notification"; // promotion
                shortAction = getString(R.string.short_title_newdiscount_notification);
            }break;
            case 3:{
                notificationChanel = "Refilling notification"; // заправка
                shortAction = getString(R.string.short_title_refilling_notification);
            }break;
            case 4:{
                notificationChanel = "Filling notification"; // suplinirea contului
                shortAction = getString(R.string.short_title_filling_notification);
            }break;
        }

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel existing = notificationManager.getNotificationChannel(notificationChanel);
            if(existing == null){
                NotificationChannel channel = new NotificationChannel(notificationChanel, notificationChanel, NotificationManager.IMPORTANCE_HIGH);
                channel.setLightColor(Color.GREEN);
                channel.enableLights(true);
                notificationManager.createNotificationChannel(channel);
            }
        }
        if(dataType == 2)
            largeIconBMP = BitmapFactory.decodeResource(getResources(), R.drawable.icon_promotion);
        else if(dataType == 3)
            largeIconBMP = BitmapFactory.decodeResource(getResources(), R.drawable.icon_refueling);
        else if(dataType == 4)
            largeIconBMP = BitmapFactory.decodeResource(getResources(), R.drawable.icon_supply);


        Realm realm = Realm.getDefaultInstance();
        Company company = realm.where(Company.class).equalTo("id", companyId).findFirst();
        if(company != null){
            companyName = company.getName();
            byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
            companyLogo = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            companyLogo = getResizedBitmap(companyLogo,45,45);
        }

        NotificationRealm saveNotification = new NotificationRealm();
        saveNotification.setClientId(clientID);
        saveNotification.setCompanyId(companyId);
        saveNotification.setTitle(messageTitle);
        saveNotification.setBody(messageBody + " | Category: " + category);
        saveNotification.setCategory(dataType);
        saveNotification.setCreateDate(new Date().getTime());

        realm.executeTransaction(realm1 -> {
            realm1.insert(saveNotification);
        });


        if(clientID != null){
            Accounts acc = realm.where(Accounts.class).equalTo("id",clientID).findFirst();
            if(acc != null)
                messageTitle = shortAction + acc.getName();
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, notificationChanel)
                .setSmallIcon(R.drawable.app_icon_rounds)
                .setContentTitle(messageTitle)
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(defaultSoundUri);

        notificationBuilder.setLargeIcon(largeIconBMP);

        if(companyName != null)
            notificationBuilder.setSubText(companyName);
        else
            notificationBuilder.setSubText(getString(R.string.app_name));


        if(dataType == 1){
            Intent intent = new Intent(this, MainActivityV1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 545 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(pendingIntent);
        }
        else if (dataType == 2){
            Intent intent = new Intent(this, MainActivityV1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 545 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(pendingIntent);
        }
        else {
            Intent intent = new Intent(this, NotificationListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id",clientID);
            intent.putExtra("fromService",true);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 2022 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(pendingIntent);
        }

        wakeUpLock();
        notificationManager.notify(new Random().nextInt(100000)/* ID of notification */, notificationBuilder.build());
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        // GET CURRENT SIZE
        int width = bm.getWidth();
        int height = bm.getHeight();
        // GET SCALE SIZE
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    private void wakeUpLock() {
        PowerManager pm = (PowerManager)MyFirebaseMessagingService.this.getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = pm.isScreenOn();

        Log.d("TAG", "screen on: "+ isScreenOn);

        if(isScreenOn == false) {
            Log.d("TAG", "screen on if: "+ isScreenOn);

            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");

            wl.acquire(10000);

            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }
    }
    //        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.vento_logo);
    //                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(companyLogo).bigLargeIcon(companyLogo));
//              .addAction(R.drawable.ic_gas_icon, "Snoze",pendingIntent);
}
