package com.lifeplusblepoc;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class LocalBroadcastEventEmitter {

    ReactApplicationContext context = null;
    private static LocalBroadcastEventEmitter instance = new LocalBroadcastEventEmitter();

    public static final LocalBroadcastEventEmitter getInstance() {
        return instance;
    }
    public LocalBroadcastEventEmitter setContext(ReactApplicationContext context) {
       this.context = context;
       return this;
    }

    public void emitLocalEvent(Intent intent) {

        if(context != null) {
            System.out.println("[LocalBroadcastEventEmitter] <emitLocalEvent> : Found react context. Sending an event - " + intent.getAction());
            context.sendBroadcast(intent);
            System.out.println("[LocalBroadcastEventEmitter] <emitLocalEvent> : Sent the event");
        }
    }

    public void emitJsEvent(String action) {
        if(context != null) {
            System.out.println("[LocalBroadcastEventEmitter] <emitJsEvent> : Found react context. Sending an event to UI - " + action);
            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(action, null);
            System.out.println("[LocalBroadcastEventEmitter] <emitJsEvent> : Sent the event to UI");
        }
    }
}
