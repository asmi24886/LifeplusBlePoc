package com.lifeplusblepoc;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;

public class BroadcasterThread extends Thread {

    ReactApplicationContext context = null;

    public BroadcasterThread(ReactApplicationContext context) {
        super();
    }

    @Override
    public void run() {
        broadcastEvent();
    }

    public void broadcastEvent() {
        Intent intent = new Intent();
        intent.setAction(AppEvents.DELEGATE_PRINT_LOG);
        System.out.println("[BroadcasterThread] <broadcastEvent> : Preparing to send an event");

        LocalBroadcastEventEmitter.getInstance().emitLocalEvent(intent);
    }
}
