package com.ninhhk.faster;

import android.graphics.Bitmap;

public class LruCache implements Cache<Key, Bitmap> {
    private static final int MAX_SIZE = 1024;

    @Override
    public Bitmap get(Key key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void put(Key key, Bitmap value) {

    }
}
