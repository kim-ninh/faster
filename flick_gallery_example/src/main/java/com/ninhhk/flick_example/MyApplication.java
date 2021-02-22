package com.ninhhk.flick_example;

import android.app.Application;

import com.ninhhk.faster.Config;
import com.ninhhk.faster.Faster;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Config config = new Config.Builder().setUseDiskCache(false)
                .enableLogging(BuildConfig.DEBUG)
                .build();
        Faster.setConfig(config);
    }
}
