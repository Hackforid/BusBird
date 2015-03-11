package com.smilehacker.busbird.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.smilehacker.busbird.R;
import com.smilehacker.busbird.adapter.DestinationListAdapter;
import com.smilehacker.busbird.app.Constants;
import com.smilehacker.busbird.model.Destination;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kleist on 15/2/5.
 */
public class DestinationListActivity extends BaseActivity {

    @InjectView(R.id.rv_des)
    RecyclerView mRvDes;
    @InjectView(R.id.btn_add)
    Button mBtnAdd;

    private DestinationListAdapter mDestinationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_des_list);
        ButterKnife.inject(this);

        mDestinationAdapter = new DestinationListAdapter(this);
        initView();
        loadData();
    }

    private void initView() {

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinationListActivity.this, MapChooseDestinationActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CHOOSE_DESTINATION);
            }
        });

        mRvDes.setLayoutManager(new LinearLayoutManager(this));
        mRvDes.setAdapter(mDestinationAdapter);
    }

    private void loadData() {
        ArrayList<Destination> destinations = (ArrayList<Destination>) Destination.getAll();
        mDestinationAdapter.setDestinations(destinations);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CHOOSE_DESTINATION:
                if (resultCode == RESULT_OK) {
                    saveDestination(data);
                }
                break;
        }
    }

    private void saveDestination(Intent data) {
        String title = data.getStringExtra(Constants.KEY_DESTINATION_TITLE);
        double lat = data.getDoubleExtra(Constants.KEY_DESTINATION_LAT, 0);
        double lon = data.getDoubleExtra(Constants.KEY_DESTINATION_LON, 0);

        Destination.add(title, lat, lon);
        loadData();
    }
}
