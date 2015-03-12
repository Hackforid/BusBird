package com.smilehacker.busbird.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * Created by kleist on 15/3/11.
 */
public class BaseRecyclerView extends RecyclerView {


    private OnItemClickListener mOnItemClickListener;

    public BaseRecyclerView(Context context) {
        super(context);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int pos);
    }
}
