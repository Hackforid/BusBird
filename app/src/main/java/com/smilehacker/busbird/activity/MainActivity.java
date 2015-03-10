package com.smilehacker.busbird.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilehacker.busbird.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        mTvDestinationTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapChooseDestinationActivity.class);
                startActivity(intent);
            }
        });
    }

}
