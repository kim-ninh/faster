package com.ninhhk.faster.pool;

import android.graphics.Bitmap;

public interface BitmapPool {

    int getMaxSize();

    void put(Bitmap bitmap);

    Bitmap get(int width, int height, Bitmap.Config config);

    void trim(int size);

    void clear();
}
