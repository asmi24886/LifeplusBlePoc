package com.lifeplusblepoc;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          packages.add(new LifeplusNativePackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
    //startService();
  }

  /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.lifeplusblepoc.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
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
