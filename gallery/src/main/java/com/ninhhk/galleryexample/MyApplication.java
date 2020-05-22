package com.ninhhk.galleryexample;

import android.app.Application;

import com.ninhhk.faster.Config;
import com.ninhhk.faster.Faster;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Config config = new Config.Builder().setUseDiskCache(false).build();
        Faster.getInstance(this).setConfig(config);
    }
}
