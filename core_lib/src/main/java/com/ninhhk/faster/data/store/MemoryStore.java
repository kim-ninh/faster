package com.ninhhk.faster.data.store;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.ninhhk.faster.Cacheable;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.Loadable;
import com.ninhhk.faster.Request;

import java.nio.ByteBuffer;

public abstract class MemoryStore
        implements Loadable<Bitmap>, Cacheable<Bitmap> {

    protected int maxCacheSize;

    public MemoryStore(int maxCacheSize){
        this.maxCacheSize = maxCacheSize;
    }

    public abstract void clear();
}
