package com.lifeplusblepoc;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

public class BootCompletedReceiver  extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context _context, Intent intent) {
        //This message is received from native end -> UiBroadcastReceiver gets event from javascript and starts a thread sending events to local native android 10 times.
        //Those events are caught here
        System.out.println("[BootCompletedReceiver] <onReceive> : Received broadcast to BootCompletedReceiver. $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        _context.startForegroundService(new Intent(_context, TestService.class));
        _context.bindService(new Intent(_context, TestService.class), this.serviceConnection, Context.BIND_AUTO_CREATE);

//        new Thread() {
//
//            @Override
//            public void run() {
//                try {
//                    Thread.currentThread().sleep(5000);
//                    System.out.println("[BootCompletedReceiver] <onReceive> : Received broadcast to BootCompletedReceiver. $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder serviceBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TestService.LocalServiceBinder binder = (TestService.LocalServiceBinder) serviceBinder;
            ServiceState._service = binder.getService();
            //isServiceBound = true;
            System.out.println("SERVICE CONNECTED");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            ServiceState._service = null;
            //isServiceBound = false;
            System.out.println("SERVICE DISCONNECTED");
        }
    };

}
