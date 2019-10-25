package com.ninhhk.faster.pool;

import android.graphics.Bitmap;

public class FasterBitmapPool implements BitmapPool {
    private BitmapPool bitmapPool = new DefaultBitmapPool();

    private static final FasterBitmapPool ourInstance = new FasterBitmapPool();

    public static FasterBitmapPool getInstance() {
        return ourInstance;
    }

    private FasterBitmapPool() {
    }

    @Override
    public int getMaxSize() {
        return bitmapPool.getMaxSize();
    }

    @Override
    public void put(Bitmap bitmap) {
        bitmapPool.put(bitmap);
    }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        return bitmapPool.get(width, height, config);
    }

    @Override
    public void trim(int size) {
        bitmapPool.trim(size);
    }

    @Override
    public void clear() {
        bitmapPool.clear();
    }
}
