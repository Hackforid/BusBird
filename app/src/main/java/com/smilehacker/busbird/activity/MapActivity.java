package com.smilehacker.busbird.activity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.smilehacker.busbird.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kleist on 15/1/20.
 */
public class MapActivity extends BaseActivity implements AMapLocationListener, LocationSource {

    private final static String TAG = MapActivity.class.getName();

    @InjectView(R.id.v_map)
    MapView mVMap;

    private AMap mAMap;
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;
    private LocationManagerProxy mLocationManagerProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map);
        ButterKnife.inject(this);
        initMapView(savedInstanceState);
    }

    private void initMapView(Bundle saveInstanceState) {
        mVMap.onCreate(saveInstanceState);
        Log.i(TAG, mVMap.toString());

        if (mAMap == null) {
            mAMap = mVMap.getMap();
        }

        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mAMap.setLocationSource(this);
        mAMap.setTrafficEnabled(true);
        mAMap.setMyLocationEnabled(true);
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);

        initCamera();
    }

    private void initCamera() {
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(20));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVMap.onPause();
        deactivate();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mVMap.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVMap.onDestroy();
    }

    /**
     * Location Listener
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mOnLocationChangedListener != null && aMapLocation != null) {
            Log.i(TAG, "errcode=" + aMapLocation.getAMapException().getErrorCode() + aMapLocation.getAMapException().getErrorMessage());
            if (aMapLocation.getAMapException().getErrorCode() == 0) {
                mOnLocationChangedListener.onLocationChanged(aMapLocation);
            }
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

    /**
     * Location Source
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
        if (mLocationManagerProxy == null) {
            mLocationManagerProxy = LocationManagerProxy.getInstance(this);
            mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork,
                    15 * 1000, 10 ,this);
        }
    }

    @Override
    public void deactivate() {
        mOnLocationChangedListener = null;
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destroy();
        }

        mLocationManagerProxy = null;
    }
}
