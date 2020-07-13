package com.lifeplusblepoc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class LocalBroadcastReceiver extends BroadcastReceiver {

    ReactApplicationContext context = null;

    public LocalBroadcastReceiver(ReactApplicationContext context) {
        super();
        this.context = context;
    }

    @Override
    public void onReceive(Context _context, Intent intent) {
        //This message is received from native end -> UiBroadcastReceiver gets event from javascript and starts a thread sending events to local native android 10 times.
        //Those events are caught here
        System.out.println("[LocalBroadcastReceiver] <onReceive> : Received broadcast from LocalBroadcastEventEmitter. ");

        if( intent.getAction().equals(AppEvents.DELEGATE_PRINT_LOG) ) {
            System.out.println("[LocalBroadcastReceiver] <onReceive> : Verified broadcast from LocalBroadcastEventEmitter. ");

            LocalBroadcastEventEmitter.getInstance().emitJsEvent(AppEvents.PRINT_LOG);
        }

    }
}
