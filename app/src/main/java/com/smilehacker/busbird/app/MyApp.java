package com.smilehacker.busbird.app;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by kleist on 15/3/11.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
