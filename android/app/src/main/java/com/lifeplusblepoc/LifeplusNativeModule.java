package com.lifeplusblepoc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import javax.annotation.Nonnull;

public class LifeplusNativeModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    public static final String REACT_CLASS = "BleNative";
    private ReactApplicationContext reactContext;

//    private UiBroadcastReceiver uiBroadcastReceiver = null;
//    private LocalBroadcastReceiver localBroadcastReceiver = null;

//    private boolean hasEventsRegistered = false;
//    private boolean isServiceRunning = false;
//    private boolean isServiceBound = false;

    //private TestService _service = null;

    public LifeplusNativeModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        //this.createAndRegisterBroadCastReceivers();
        LocalBroadcastEventEmitter.getInstance().setReactContext(this.reactContext);
        System.out.println("LifeplusNativeModule constructor has been initiated successfully. Attached context to event emitters and event receivers.");
    }

    @Nonnull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void startService() {

        if(ServiceState._service != null)
            return;

        System.out.println("Start service called");

        this.reactContext.startService(new Intent(this.reactContext, TestService.class));
        this.reactContext.bindService(new Intent(this.getCurrentActivity(), TestService.class), this.serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @ReactMethod
    public void stopService() {

        System.out.println("Stop service called");

        try {

            if(ServiceState._service != null) {
                this.reactContext.unbindService(serviceConnection);
                //this._service = null;
                System.out.println("Unbinding of service done because stop service was called.");
            }

            System.out.println("Now going for stopping service");
            this.reactContext.stopService(new Intent(this.reactContext, TestService.class));
            //this.isServiceRunning = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR IN stopService - " + e.getMessage());
        }
    }

    //This should start the emit chain
    @ReactMethod
    private void testEmit() {
        //CAN ALSO BE BOUND TO SERVICE IF emitEventStart is non static but then check if service instance available. 
        //Made static so that method may be immediately called to check broadcast reveivers and event receivers and if ui thread is alive without service
        //We know ui thread is alive when service started but stops on quit if no service is on. 
        //WHich should we do ??? :(
        if(ServiceState._service != null) {
            //this._service.emitEventStart();
        }

        TestService.emitEventStart(); //Can also be made non static and bound by the service
    }

    @ReactMethod
    public void isServiceAlive(Callback callback) {

        if(callback != null) {
            try {
                callback.invoke(ServiceState._service != null);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ReactMethod
    public void send(String command, Promise promise) {

        WritableMap _map = Arguments.createMap();
        try {
            String message = "Received command - " + command;
            _map.putString("message", message);
            promise.resolve(_map);

            RpcManager.getInstance().receive(command);
        }
        catch (Exception e) {
            promise.reject(e);
        }

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

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

        //unRegisterBroadcastReceivers();
    }

//    private void createAndRegisterBroadCastReceivers() {
//        this.uiBroadcastReceiver     = new UiBroadcastReceiver();
//        this.localBroadcastReceiver  = new LocalBroadcastReceiver();
//        System.out.println("Created broadcast receivers");
//        registerBroadcastReceivers();
//    }
//
//    private void registerBroadcastReceivers() {
//        IntentFilter filter1 = new IntentFilter();
//        filter1.addAction(AppEvents.START_BROADCAST);
//
//        IntentFilter filter2 = new IntentFilter();
//        filter2.addAction(AppEvents.DELEGATE_PRINT_LOG);
//
//        this.reactContext.registerReceiver(this.uiBroadcastReceiver, filter1);
//        this.reactContext.registerReceiver(this.localBroadcastReceiver, filter2);
//
//        this.hasEventsRegistered = true;
//        System.out.println("Registered broadcast receivers");
//    }
//
//    private void unRegisterBroadcastReceivers() {
//
//        try {
//            this.hasEventsRegistered = false;
//            this.reactContext.unregisterReceiver(this.uiBroadcastReceiver);
//            this.reactContext.unregisterReceiver(this.localBroadcastReceiver);
//            System.out.println("Un Registered broadcast receivers");
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}
