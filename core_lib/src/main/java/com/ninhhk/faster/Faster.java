package com.ninhhk.faster;

import android.content.Context;

import androidx.annotation.NonNull;

public class Faster {
    private static Faster mInstance = null;
    private final ImageLoader imageLoader;
    private Request.Builder mBuilder;
    private static Config DEFAULT_CONFIG = new Config.Builder().build();
    private static Config sConfig = DEFAULT_CONFIG;

    private Faster(Context context) {
        imageLoader = new ImageLoader(context, sConfig);
    }

    public static Request.Builder with(@NonNull Context context) {
        Context applicationContext = context.getApplicationContext();
        mInstance = mGetInstance(applicationContext);
        return mInstance.getRequestBuilder(applicationContext);
    }

    private static Faster mGetInstance(Context context) {
        if (mInstance == null) {
            synchronized (Faster.class) {
                if (mInstance == null) {
                    mInstance = new Faster(context);
                }
            }
        }
        return mInstance;
    }

    public static Faster getInstance(Context context){
        return mGetInstance(context);
    }

    public Request.Builder getRequestBuilder(@NonNull Context context) {
        mBuilder = new Request.Builder(imageLoader, context);
        return mBuilder;
    }

    public void clearCache() {
        imageLoader.clearCache();
    }

    public static void setConfig(@NonNull Config config){
        Faster.sConfig = config;
    }
}
