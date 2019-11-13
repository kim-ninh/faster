package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.ninhhk.faster.data.source.DataSource;

public class LruCacheStrategy implements Cache<DataSource<?>, Bitmap> {

    private static final int MAX_SIZE = 1024;
    private LruCache<DataSource<?>, Bitmap> lruCache;

    public LruCacheStrategy() {
        lruCache = new LruCache<>(MAX_SIZE);

    }

    public LruCacheStrategy(int size){
        lruCache = new LruCache<>(size);
    }

    public LruCacheStrategy(LruCache lruCache) {
        this.lruCache = lruCache;

    }


    @Override
    public Bitmap get(DataSource<?> key) {
        return lruCache.get(key);
    }

    @Override
    public int size() {
        return lruCache.size();
    }

    @Override
    public void put(DataSource<?> key, Bitmap value) {
        lruCache.put(key, value);
    }

}
