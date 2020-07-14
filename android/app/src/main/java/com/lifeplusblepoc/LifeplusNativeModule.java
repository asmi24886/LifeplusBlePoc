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
        this.createAndRegisterBroadCastReceivers();
        LocalBroadcastEventEmitter.getInstance().setContext(this.reactContext);
        System.out.println("LifeplusNativeModule constructor has been initiated successfully. Attached context to event emitters and event receivers.");
    }

    @Nonnull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void startService() {

        if(this.isServiceRunning == true)
            return;

        System.out.println("Start service called");

        this.reactContext.startService(new Intent(this.reactContext, TestService.class));
        this.isServiceRunning = true;
        this.reactContext.bindService(new Intent(this.getCurrentActivity(), TestService.class), this.serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @ReactMethod
    public void stopService() {

        System.out.println("Stop service called");

        try {

            if(this._service != null && this.isServiceBound) {
                this.reactContext.unbindService(serviceConnection);
                this._service = null;
                this.isServiceBound = false;
                System.out.println("Unbinding of service done because stop service was called.");
            }

            System.out.println("Now going for stopping service");
            this.reactContext.stopService(new Intent(this.reactContext, TestService.class));
            this.isServiceRunning = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR IN stopService - " + e.getMessage());
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

    @ReactMethod
    private void testEmit() {

        if(this._service != null && this.isServiceBound) {
            this._service.emitEventStart();
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

        this.reactContext.registerReceiver(this.uiBroadcastReceiver, filter1);
        this.reactContext.registerReceiver(this.localBroadcastReceiver, filter2);

        this.hasEventsRegistered = true;
        System.out.println("Registered broadcast receivers");
    }

    private void unRegisterBroadcastReceivers() {

        try {
            this.hasEventsRegistered = false;
            this.reactContext.unregisterReceiver(this.uiBroadcastReceiver);
            this.reactContext.unregisterReceiver(this.localBroadcastReceiver);
            System.out.println("Un Registered broadcast receivers");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
