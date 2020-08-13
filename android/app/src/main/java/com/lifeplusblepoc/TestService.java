package com.lifeplusblepoc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TestService extends Service {

    private static final int SERVICE_NOTIFICATION_ID = 12345;
    private static final String CHANNEL_ID = "TEST";

    private UiBroadcastReceiver uiBroadcastReceiver = null;
    private LocalBroadcastReceiver localBroadcastReceiver = null;

    private final IBinder _binder = new LocalServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        createAndRegisterBroadCastReceivers();
        LocalBroadcastEventEmitter.getInstance().setContext(this.getApplicationContext());
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        unRegisterBroadcastReceivers();
        LocalBroadcastEventEmitter.getInstance().setContext(null);
        ServiceState._service = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Test service")
                .setContentText("Running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .build();
        startForeground(SERVICE_NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            channel.setDescription("This is a test service");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void emitEventStart() {
        System.out.println("[TestService] <emitEventStart> : Test service start event will emit");
        Intent intent = new Intent();
        intent.setAction(AppEvents.START_BROADCAST);
        LocalBroadcastEventEmitter.getInstance().emitLocalEvent(intent);
        System.out.println("[TestService] <emitEventStart> : Test service start event emitted");
    }

    public class LocalServiceBinder extends Binder {
        TestService getService() {
            return TestService.this;
        }
    }

    private void createAndRegisterBroadCastReceivers() {
        this.uiBroadcastReceiver     = new UiBroadcastReceiver();
        this.localBroadcastReceiver  = new LocalBroadcastReceiver();
        System.out.println("Created broadcast receivers");
        registerBroadcastReceivers();
    }

    private void registerBroadcastReceivers() {
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(AppEvents.START_BROADCAST);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(AppEvents.DELEGATE_PRINT_LOG);

        this.getApplicationContext().registerReceiver(this.uiBroadcastReceiver, filter1);
        this.getApplicationContext().registerReceiver(this.localBroadcastReceiver, filter2);

        ServiceState.hasEventsRegistered = true;
        System.out.println("Registered broadcast receivers");
    }

    private void unRegisterBroadcastReceivers() {

        try {
            ServiceState.hasEventsRegistered = false;
            this.getApplicationContext().unregisterReceiver(this.uiBroadcastReceiver);
            this.getApplicationContext().unregisterReceiver(this.localBroadcastReceiver);
            System.out.println("Un Registered broadcast receivers");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
