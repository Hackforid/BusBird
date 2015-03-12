package com.smilehacker.busbird.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.smilehacker.busbird.app.Constants;
import com.smilehacker.busbird.tools.DLog;
import com.smilehacker.busbird.utils.GPSUtil;

/**
 * Created by kleist on 15/3/12.
 */
public class LocationService extends Service{

    private LocationManagerProxy mLocationManagerProxy;
    private double mDesLat;
    private double mDesLon;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDesLat = intent.getDoubleExtra(Constants.KEY_DESTINATION_LAT, 0);
        mDesLon = intent.getDoubleExtra(Constants.KEY_DESTINATION_LON, 0);
        locate();
        return super.onStartCommand(intent, flags, startId);
    }

    private void locate() {
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 0,
                new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
                            double lat = aMapLocation.getLatitude();
                            double lon = aMapLocation.getLongitude();
                            DLog.i("locate at lat=" + lat + " lon=" + lon);
                            DLog.i("dest=" + GPSUtil.getDistance(lat, lon, mDesLat, mDesLon));
                            double distance = GPSUtil.getDistance(lat, lon, mDesLat, mDesLon);
                            broadcastLocation(lat, lon, distance);
                            stopSelf();
                        }
                    }

                    @Override
                    public void onLocationChanged(Location location) {

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
                }
        );

    }

    private void broadcastLocation(double lat, double lon, double distance) {
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_LOCATE);

        intent.putExtra(Constants.KEY_LOCATE_LAT, lat);
        intent.putExtra(Constants.KEY_LOCATE_LON, lon);
        intent.putExtra(Constants.KEY_LOCATE_DISTANCE, distance);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
