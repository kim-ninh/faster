package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.util.LruCache;

public class LruCacheStrategy implements Cache<String, Bitmap> {

    private static final int MAX_SIZE = 1024;
    private LruCache<String, Bitmap> lruCache;

    LruCacheStrategy() {
        lruCache = new LruCache(MAX_SIZE);

    }

    public LruCacheStrategy(LruCache lruCache) {
        this.lruCache = lruCache;

    }

    @Override
    public Bitmap get(String key) {
        return lruCache.get(key);
    }

    @Override
    public int size() {
        return lruCache.size();
    }

    @Override
    public void put(String key, Bitmap value) {
        lruCache.put(key, value);
    }
}
