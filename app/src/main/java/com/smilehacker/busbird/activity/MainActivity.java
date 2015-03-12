package com.smilehacker.busbird.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilehacker.busbird.R;
import com.smilehacker.busbird.app.Constants;
import com.smilehacker.busbird.app.SPManager;
import com.smilehacker.busbird.model.Destination;
import com.smilehacker.busbird.service.LocationService;
import com.smilehacker.busbird.tools.DLog;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {


    @InjectView(R.id.tv_destination_title)
    TextView mTvDestinationTitle;
    @InjectView(R.id.tv_destination_address)
    TextView mTvDestinationAddress;
    @InjectView(R.id.rl_header)
    RelativeLayout mRlHeader;
    @InjectView(R.id.tv_distance)
    TextView mTvDistance;
    @InjectView(R.id.rl_distance)
    RelativeLayout mRlDistance;

    private Destination mDestination;
    private BroadcastReceiver mLocateBroadcastReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;
    private IntentFilter mLocateIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        init();
        initView();
        initData();
    }

    private void init() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocateIntentFilter = new IntentFilter();
        mLocateIntentFilter.addAction(Constants.BROADCAST_LOCATE);

        mLocateBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double lat = intent.getDoubleExtra(Constants.KEY_LOCATE_LAT, 0);
                double lon = intent.getDoubleExtra(Constants.KEY_LOCATE_LON, 0);
                double distance = intent.getDoubleExtra(Constants.KEY_LOCATE_DISTANCE, 0);
                mTvDistance.setText(formatDistance(distance));
            }
        };
    }


    private void initView() {
        mTvDestinationTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DestinationListActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CHOOSE_DESTINATION);
            }
        });

        mTvDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLog.i("start locate");
                Intent intent = new Intent(MainActivity.this, LocationService.class);
                intent.putExtra(Constants.KEY_DESTINATION_LAT, mDestination.lat);
                intent.putExtra(Constants.KEY_DESTINATION_LON, mDestination.lon);
                startService(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalBroadcastManager.registerReceiver(mLocateBroadcastReceiver, mLocateIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalBroadcastManager.unregisterReceiver(mLocateBroadcastReceiver);
    }

    private void initData() {
        mDestination = getDestination();
        showDestination();
    }

    private void showDestination() {
        if (mDestination != null) {
            mTvDestinationTitle.setText(mDestination.title);
        } else {
            mTvDestinationTitle.setText("请选择");
            mTvDistance.setText("0 Km");
        }
    }

    private Destination getDestination() {
        long desId = SPManager.inst(this).getDestinationId();
        Destination des = Destination.get(desId);
        return des;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CHOOSE_DESTINATION && resultCode == RESULT_OK) {
            loadDestination(data);
        }
    }

    private void loadDestination(Intent data) {
        long id = data.getLongExtra(Constants.KEY_DESTINATION_ID, 0);
        SPManager.inst(this).setDestinationId(id);
        initData();
    }

    private String formatDistance(double distance) {
        long n = Math.round(distance / 1000);
        return String.format("%1$d Km", n);
    }


}
