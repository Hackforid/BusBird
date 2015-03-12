package com.smilehacker.busbird.utils;

import android.location.Location;

/**
 * Created by kleist on 15/3/12.
 */
public class GPSUtil {


    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results=new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

}
