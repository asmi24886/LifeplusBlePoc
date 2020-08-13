package com.lifeplusblepoc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReactMethod;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "LifeplusBlePoc";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //startService();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    //stopService();
    super.onDestroy();
  }

  public void startService() {

    if(ServiceState._service != null)
      return;

    System.out.println("Start service called");

    getApplicationContext().startService(new Intent(getApplicationContext(), TestService.class));
    getApplicationContext().bindService(new Intent(getApplicationContext(), TestService.class), this.serviceConnection, Context.BIND_AUTO_CREATE);
  }

  public void stopService() {

    System.out.println("Stop service called");

    try {

      if(ServiceState._service != null) {
        getApplicationContext().unbindService(serviceConnection);
        //this._service = null;
        System.out.println("Unbinding of service done because stop service was called.");
      }

      System.out.println("Now going for stopping service");
      getApplicationContext().stopService(new Intent(getApplicationContext(), TestService.class));
      //this.isServiceRunning = false;
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
