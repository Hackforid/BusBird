package com.smilehacker.busbird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.smilehacker.busbird.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kleist on 15/3/10.
 */
public class SearchSuggestionRvAdapter extends RecyclerView.Adapter<SearchSuggestionRvAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private ArrayList<PoiItem> mPoiItems;
    private OnPoiItemClickListener mOnPoiItemClickListener;

    public SearchSuggestionRvAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mPoiItems = new ArrayList<>();
    }

    public void setPoiItems(ArrayList<PoiItem> poiItems) {
        mPoiItems.clear();
        mPoiItems.addAll(poiItems);
        notifyDataSetChanged();
    }

    public void setOnPoiItemClickListener(OnPoiItemClickListener listener) {
        mOnPoiItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_search_suggestion, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final PoiItem item = mPoiItems.get(i);
        viewHolder.mTvName.setText(item.getTitle());
        viewHolder.mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPoiItemClickListener != null) {
                    mOnPoiItemClickListener.onPoiItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPoiItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public static interface OnPoiItemClickListener {
        public void onPoiItemClick(PoiItem poiItem);
    }
}
