package com.lifeplusblepoc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;

public class UiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context _context, Intent intent) {
        //This message is received by triggering an event from the Javascript end
        System.out.println("[UiBroadcastReceiver] <onReceive> : Received broadcast from UI javascript.");

        if( intent.getAction().equals(AppEvents.START_BROADCAST) ) {
            System.out.println("[UiBroadcastReceiver] <onReceive> : Verified broadcast from UI javascript. Starting local broadcasting thread");
            BroadcasterThread _t = new BroadcasterThread();
            _t.start();
        }

    }
}
