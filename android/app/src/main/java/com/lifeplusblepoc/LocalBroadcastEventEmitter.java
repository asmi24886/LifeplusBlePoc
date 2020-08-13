package com.lifeplusblepoc;

import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class LocalBroadcastEventEmitter {

    ReactApplicationContext context = null;
    Context _context = null;
    private static LocalBroadcastEventEmitter instance = new LocalBroadcastEventEmitter();

    public static final LocalBroadcastEventEmitter getInstance() {
        return instance;
    }

    public LocalBroadcastEventEmitter setReactContext(ReactApplicationContext context) {
       this.context = context;
       return this;
    }

    public LocalBroadcastEventEmitter setContext(Context context) {
        this._context = context;
        return this;
    }

    public void emitLocalEvent(Intent intent) {

        if(_context != null) {
            System.out.println("[LocalBroadcastEventEmitter] <emitLocalEvent> : Found react context. Sending an event - " + intent.getAction());
            _context.sendBroadcast(intent);
            System.out.println("[LocalBroadcastEventEmitter] <emitLocalEvent> : Sent the event");
        }
    }

    public void emitJsEvent(String action, String data) {
        emitJsEvent(action, action, data);
    }

    public void emitJsEvent(String action, String command, String data) {
        data = data == null ? "{}" : data;

        WritableMap _map = Arguments.createMap();
        _map.putString("command", command);
        _map.putString("data", data);

        if(context != null) {
            System.out.println("[LocalBroadcastEventEmitter] <emitJsEvent> : Found react context. Sending an event to UI - " + action);
            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(action, _map);
            System.out.println("[LocalBroadcastEventEmitter] <emitJsEvent> : Sent the event to UI");
        }
    }
    public void emitJsEvent(String action) {
        emitJsEvent(action, null);
    }
}
