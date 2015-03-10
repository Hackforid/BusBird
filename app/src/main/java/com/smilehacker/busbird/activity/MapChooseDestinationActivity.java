package com.smilehacker.busbird.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.smilehacker.busbird.R;
import com.smilehacker.busbird.adapter.SearchSuggestionRvAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kleist on 15/2/5.
 */
public class MapChooseDestinationActivity extends BaseActivity implements LocationSource, AMapLocationListener{

    private final static String TAG = MapChooseDestinationActivity.class.getName();

    @InjectView(R.id.v_map)
    MapView mVMap;
    @InjectView(R.id.et_address)
    EditText mEtAddress;
    @InjectView(R.id.btn_search)
    Button mBtnSearch;
    @InjectView(R.id.rl_suggestion)
    RelativeLayout mRlSuggestion;
    @InjectView(R.id.rv_suggestion)
    RecyclerView mRvSuggestion;
    @InjectView(R.id.rl_target)
    RelativeLayout mRlTarget;
    @InjectView(R.id.tv_target_title)
    TextView mTvTargetTitle;
    @InjectView(R.id.btn_set_target)
    Button mBtnSetTarget;

    private AMap mAMap;
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;
    private LocationManagerProxy mLocationManagerProxy;
    private Bundle mLocationBundle;
    private String mCity;

    private SearchSuggestionRvAdapter mSearchSuggestionRvAdapter;

    private PoiItem mSelectedPoiItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map_choose_destination);
        ButterKnife.inject(this);

        mSearchSuggestionRvAdapter = new SearchSuggestionRvAdapter(this);

        initView();
        initMap(savedInstanceState);
    }

    private void initView() {
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRlTarget.setVisibility(View.GONE);
                search();
            }
        });

        initSuggestionView();

        mBtnSetTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedPoiItem != null) {
                    Log.i(TAG, "PoiItem" + mSelectedPoiItem.getTitle() + " "
                            + mSelectedPoiItem.getAdName() + " "
                            + mSelectedPoiItem.getDistance() + " "
                            + mSelectedPoiItem.getAdName() + " "
                            + mSelectedPoiItem.getEmail() + " "
                            + mSelectedPoiItem.getProvinceName() + " "
                            + mSelectedPoiItem.getSnippet() + " "
                            + mSelectedPoiItem.getTypeDes() + " "
                            + mSelectedPoiItem.getWebsite() + " "
                            + mSelectedPoiItem.getDirection());
                }
            }
        });
    }

    private void initSuggestionView() {
        mRvSuggestion.setLayoutManager(new LinearLayoutManager(this));
        mRvSuggestion.setAdapter(mSearchSuggestionRvAdapter);

        mSearchSuggestionRvAdapter.setOnPoiItemClickListener(new SearchSuggestionRvAdapter.OnPoiItemClickListener() {
            @Override
            public void onPoiItemClick(PoiItem poiItem) {
                mAMap.clear();
                ArrayList<PoiItem> items = new ArrayList<>();
                items.add(poiItem);
                PoiOverlay poiOverlay = new PoiOverlay(mAMap, items);
                poiOverlay.removeFromMap();
                poiOverlay.addToMap();
                poiOverlay.zoomToSpan();
                mRlSuggestion.setVisibility(View.GONE);

                mRlTarget.setVisibility(View.VISIBLE);
                mTvTargetTitle.setText(poiItem.getTitle());
                mSelectedPoiItem = poiItem;
            }
        });

    }

    private void initMap(Bundle saveInstanceState) {
        mVMap.onCreate(saveInstanceState);
        Log.i(TAG, mVMap.toString());

        if (mAMap == null) {
            mAMap = mVMap.getMap();
        }

        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mAMap.setLocationSource(this);
        mAMap.setMyLocationEnabled(true);
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);

        initCamera();
    }

    private void initCamera() {
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(20));
    }

    private void search() {
        if (TextUtils.isEmpty(mCity)) {
            Toast.makeText(this, "定位中 请稍等", Toast.LENGTH_SHORT).show();
            return;
        }

        PoiSearch.Query query = new PoiSearch.Query(mEtAddress.getText().toString(), "", mCity);
        query.setPageSize(20);
        query.setPageNum(0);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int rCode) {
                if (rCode != 0) {
                    Toast.makeText(MapChooseDestinationActivity.this, "search fail", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<PoiItem> poiItems = poiResult.getPois();
//                for (PoiItem item : poiItems) {
//                    Log.i(TAG, item.toString());
//                }

                mRlSuggestion.setVisibility(View.VISIBLE);
                mSearchSuggestionRvAdapter.setPoiItems(poiItems);

            }

            @Override
            public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

            }
        });

        poiSearch.searchPOIAsyn();

    }

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
            mCity = aMapLocation.getCityCode();
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
                    15 * 1000, 1, this);
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
