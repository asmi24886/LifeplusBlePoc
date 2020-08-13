package com.lifeplusblepoc;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;

public class BroadcasterThread extends Thread {

    @Override
    public void run() {
        int i = 1;

        //SEND THIS BROADCAST EVENT 3 times just to check whether this is possible or not
        try {
            while (i++ != 3) {
                broadcastEvent();
                Thread.currentThread().sleep(3000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //broadcastEvent();
    }

    public void broadcastEvent() {
        Intent intent = new Intent();
        intent.setAction(AppEvents.DELEGATE_PRINT_LOG);
        System.out.println("[BroadcasterThread] <broadcastEvent> : Preparing to send an event");

        LocalBroadcastEventEmitter.getInstance().emitLocalEvent(intent);
    }
}
