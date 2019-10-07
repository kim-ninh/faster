package com.ninhhk.faster;

public class Faster {
    private static Faster mInstance = null;
    private Request.RequestBuilder mRequestBuilder;
    private Cache memCache;
    private ImageDecoder imageDecodeHelper;

    private Faster() {
        mRequestBuilder = new Request.RequestBuilder();
    }

    public static Faster getInstance() {
        if (mInstance == null) {
            synchronized (Faster.class) {
                if (mInstance == null) {
                    mInstance = new Faster();
                }
            }
        }
        return mInstance;
    }

    public Request.RequestBuilder load(String url) {
        return mRequestBuilder.load(url);
    }

    public void changeDecodeHelper(ImageDecoder imageDecodeHelper) {
        this.imageDecodeHelper = imageDecodeHelper;
    }

    public void changeCacheStrategy(Cache memCache) {
        this.memCache = memCache;
    }
}
