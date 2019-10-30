package com.ninhhk.faster;

import android.content.Context;

public class Faster {
    private static Faster mInstance = null;
    private final ImageLoader imageLoader;
    private Request.Builder mBuilder;


    private Faster(Context context) {
        imageLoader = new MyImageLoader(context);
        mBuilder = new Request.Builder(imageLoader);

    }

    public static Request.Builder with(Context context) {
        mInstance = getInstance(context.getApplicationContext());
        return mInstance.getRequestBuilder();
    }



    private static Faster getInstance(Context context) {
        if (mInstance == null) {
            synchronized (Faster.class) {
                if (mInstance == null) {
                    mInstance = new Faster(context);
                }
            }
        }
        return mInstance;
    }

    public static Faster getInstance(){
        return mInstance;
    }

    public Request.Builder getRequestBuilder() {
        return mBuilder;
    }

    public void clearCache() {
        imageLoader.clearCache();
    }
}
