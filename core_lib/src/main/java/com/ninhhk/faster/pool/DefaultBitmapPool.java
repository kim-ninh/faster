package com.ninhhk.faster.pool;

import android.graphics.Bitmap;

// this is a pool that create a new bitmap every time has request
// it size's always equals 0
public class DefaultBitmapPool implements BitmapPool {
    @Override
    public int getMaxSize() {
        return 0;
    }

    @Override
    public void put(Bitmap bitmap) { }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        return Bitmap.createBitmap(width, height, config);
    }

    @Override
    public void trim(int size) { }

    @Override
    public void clear() { }
}
