/*
    The class that is really responsible for tracking the user's movement.
    Also works in the background as a service.
 */

package com.example.hp_pc.olympian;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

public class OdometerService extends Service {
    //Service binder
    private final IBinder binder = new OdomterBinder();

    //User movement generated data
    private static double distanceInMeters;
    private static Location lastLocation = null;

    //Empty constructor
    public OdometerService() {

    }

    //Nested class for binder
    public class OdomterBinder extends Binder {
        OdometerService getOdometer() {
            return OdometerService.this;
        }
    }

    //Whenever we need to bind the object as a service
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //OnCreate method is called implicitly
    @Override
    public void onCreate() {
        super.onCreate();

        //Set up a new LocationListener to monitor user movement
        LocationListener listener = new LocationListener() {
            //Implement methods

            //Only method that is important to us.
            // Called automatically whenever location is changed according to calling parameters
            @Override
            public void onLocationChanged(Location location) {
                if (lastLocation == null) {
                    lastLocation = location;
                }
                distanceInMeters += location.distanceTo(lastLocation);
                lastLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //New Location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //check if location is updated by (10seconds and 10m)  between updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, listener);
    }

    //Converting into kilometers.
    public double getKms(){
        return this.distanceInMeters/1000.0;
    }

    //Return last known location of the user
    public Location getLastLocation()
    {
        return lastLocation;
    }

    //Reset values
    public void endOdometerService()
    {
        distanceInMeters = 0;
        lastLocation = null;
    }
}
