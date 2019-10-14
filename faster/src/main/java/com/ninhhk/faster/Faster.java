package com.ninhhk.faster;

import android.content.Context;

import com.ninhhk.faster.decoder.ImageDecoder;
import com.ninhhk.faster.decoder.MatchTargetDimensionImageDecoder;

public class Faster {
    private static Faster mInstance = null;
    private final ImageLoader imageLoader;
    private Request.RequestBuilder mRequestBuilder;
    private Cache memCache;
    private ImageDecoder imageDecoder;
    private Context context;

    private Faster(Context context) {
        memCache = new LruCacheStrategy();
        imageDecoder = new MatchTargetDimensionImageDecoder();
        imageLoader = new ImageLoader(context);
        mRequestBuilder = new Request.RequestBuilder(imageLoader);
        this.context = context;
    }

    public static Faster getInstance(Context context) {
        if (mInstance == null) {
            synchronized (Faster.class) {
                if (mInstance == null) {
                    mInstance = new Faster(context);
                }
            }
        }
        return mInstance;
    }

    public Request.RequestBuilder load(String url) {
        return mRequestBuilder.load(url);
    }

    public void changeDecodeHelper(ImageDecoder imageDecodeHelper) {
        this.imageDecoder = imageDecodeHelper;
    }

    public void changeCacheStrategy(Cache memCache) {
        this.memCache = memCache;
    }
}
