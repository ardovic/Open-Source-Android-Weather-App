package com.ardovic.weatherappprototype.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class LocationService extends Service {

    private class MyLocationListnener implements LocationListener {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        // this method calls when requestSingleUpdate method invokes
        public void onLocationChanged(@NonNull Location location) {
            Double lat = location.getLatitude();
            Double longi = location.getLongitude();

            // sending coordinates to main activity via a broadcast message
            Intent intent = new Intent(ACTION);
            intent.putExtra("result",1);
            intent.putExtra("lat", lat);
            intent.putExtra("longi", longi);
            mLocalBroadcastManager.sendBroadcast(intent);
            stopSelf();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }
    private LocalBroadcastManager mLocalBroadcastManager;
    public static final String ACTION = "com.my.app.MyCustomLocationService";

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // initialization of LocationManager and LocationListener
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new MyLocationListnener();
                // checking for permissions
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // broadcasting 0 as permission is not granted by user
                    Intent intent = new Intent(ACTION);
                    intent.putExtra("result",0);
                    mLocalBroadcastManager.sendBroadcast(intent);
                    stopSelf();
                }
                else {
                    // requesting for location updates(coordinates) using callback function
                    if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    {
                        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener, Looper.getMainLooper());
                    }
                    else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, Looper.getMainLooper());
                    }
                    else
                    {
                        // broadcasting 0 as location is off on device
                        Intent intent = new Intent(ACTION);
                        intent.putExtra("result",2);
                        mLocalBroadcastManager.sendBroadcast(intent);
                        stopSelf();
                    }
                }
            }
        });
        // this will call run method of thread
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}