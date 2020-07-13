package com.lifeplusblepoc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import javax.annotation.Nonnull;

public class LifeplusNativeModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    public static final String REACT_CLASS = "BleNative";
    private ReactApplicationContext reactContext;

    private UiBroadcastReceiver uiBroadcastReceiver = null;
    private LocalBroadcastReceiver localBroadcastReceiver = null;

    private boolean hasEventsRegistered = false;
    private boolean isServiceRunning = false;
    private boolean isServiceBound = false;

    private TestService _service = null;

    public LifeplusNativeModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        LocalBroadcastEventEmitter.getInstance().setContext(this.reactContext);
    }

    @Nonnull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void startService() {

        if(hasEventsRegistered == true)
            return;

        System.out.println("Start service called");

        unRegisterBroadcastReceivers();

        uiBroadcastReceiver     = new UiBroadcastReceiver(this.reactContext);
        localBroadcastReceiver  =  new LocalBroadcastReceiver(this.reactContext);

        registerBroadcastReceivers();
        hasEventsRegistered = true;

        if(isServiceRunning == true)
            return;

        this.reactContext.startService(new Intent(this.reactContext, TestService.class));
        isServiceRunning = true;

        this.reactContext.bindService(new Intent(reactContext, TestService.class), serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @ReactMethod
    public void stopService() {

        System.out.println("Stop service called");

        try {
            this.reactContext.stopService(new Intent(this.reactContext, TestService.class));
            isServiceRunning = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        unRegisterBroadcastReceivers();
    }

    @ReactMethod
    private void testEmit() {

        if(_service != null && isServiceBound) {
            _service.emitEventStart();
        }
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        unRegisterBroadcastReceivers();
    }

    private void registerBroadcastReceivers() {
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(AppEvents.START_BROADCAST);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(AppEvents.DELEGATE_PRINT_LOG);

        reactContext.registerReceiver(uiBroadcastReceiver, filter1);
        reactContext.registerReceiver(localBroadcastReceiver, filter2);
        System.out.println("Registered broadcast receivers");
    }

    private void unRegisterBroadcastReceivers() {

        try {
            hasEventsRegistered = false;
            reactContext.unregisterReceiver(uiBroadcastReceiver);
            reactContext.unregisterReceiver(localBroadcastReceiver);
            System.out.println("Un Registered broadcast receivers");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder serviceBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TestService.LocalServiceBinder binder = (TestService.LocalServiceBinder) serviceBinder;
            _service = binder.getService();
            isServiceBound = true;
            System.out.println("SERVICE CONNECTED");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            _service = null;
            isServiceBound = false;
            System.out.println("SERVICE DISCONNECTED");
        }
    };
}
