package com.ninhhk.faster;

import android.content.Context;

public class Faster {
    private static Faster mInstance = null;
    private final ImageLoader imageLoader;
    private Request.Builder mBuilder;
    private Config DEFAULT_CONFIG = new Config.Builder().build();


    private Faster(Context context) {
        imageLoader = new MyImageLoader(context);
        imageLoader.setConfig(DEFAULT_CONFIG);
    }

    public static Request.Builder with(Context context) {
        mInstance = mGetInstance(context.getApplicationContext());
        return mInstance.getRequestBuilder();
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

    public Request.Builder getRequestBuilder() {
        mBuilder = new Request.Builder(imageLoader);
        return mBuilder;
    }

    public void clearCache() {
        imageLoader.clearCache();
    }

    public void setConfig(Config config){
        imageLoader.setConfig(config);
    }
}
