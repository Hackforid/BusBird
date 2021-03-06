package com.smilehacker.busbird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smilehacker.busbird.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.smilehacker.busbird.model.Destination;

/**
 * Created by kleist on 15/2/5.
 */
public class DestinationListAdapter extends RecyclerView.Adapter<DestinationListAdapter.ViewHolder> {

    private List<Destination> mDestinations;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public DestinationListAdapter(Context context) {
        mDestinations = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setDestinations(List<Destination> destinations) {
        mDestinations.clear();
        mDestinations.addAll(destinations);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_des, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Destination destination = mDestinations.get(position);
        holder.tvTitle.setText(destination.title);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, destination);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDestinations.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_map)
        ImageView ivMap;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        public View view;


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            this.view = view;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int pos, Destination destination);
    }
}
