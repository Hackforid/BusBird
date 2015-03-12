package com.smilehacker.busbird.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kleist on 15/3/11.
 */
public class SPManager {

    private static SPManager mInstance;

    private SharedPreferences mSharedPreferences;

    private final static String KEY_DEFAULT = "key_default";
    private final static String KEY_DEFAULT_DESTINATION_ID = "key_default_destination_id";

    public static SPManager inst(Context context) {
        if (mInstance == null) {
            synchronized (SPManager.class) {
                if (mInstance == null) {
                    mInstance = new SPManager(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    private SPManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(KEY_DEFAULT, 0);
    }

    public void setDestinationId(long id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(KEY_DEFAULT_DESTINATION_ID, id);
        editor.commit();
    }

    public long getDestinationId() {
        return mSharedPreferences.getLong(KEY_DEFAULT_DESTINATION_ID, 0);
    }
}
