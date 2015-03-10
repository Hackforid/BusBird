package com.smilehacker.busbird.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.smilehacker.busbird.R;
import com.smilehacker.busbird.adapter.DestinationListAdapter;
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
        initData();
    }

    private void initView() {

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRvDes.setAdapter(mDestinationAdapter);
    }

    private void initData() {
        ArrayList<Destination> destinations = Destination.getAll();
        mDestinationAdapter.setDestinations(destinations);
    }
}
