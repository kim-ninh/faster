package com.ninhhk.faster;

public class Faster {
    private static Faster mInstance = null;
    private final ImageLoader imageLoader;
    private Request.RequestBuilder mRequestBuilder;
    private Cache memCache;
    private ImageDecoder imageDecoder;

    private Faster() {
        memCache = new LruCacheStrategy();
        imageDecoder = new MatchTargetDimensionImageDecoder();
        imageLoader = new ImageLoader();
        mRequestBuilder = new Request.RequestBuilder(imageLoader);
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
        this.imageDecoder = imageDecodeHelper;
    }

    public void changeCacheStrategy(Cache memCache) {
        this.memCache = memCache;
    }
}
