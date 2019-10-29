package com.ninhhk.faster.data.store;

import android.content.Context;
import android.graphics.Bitmap;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;

public abstract class MemoryStore {

    protected DiskStore diskStore;

    protected BitmapStore bitmapStore;

    public MemoryStore(DiskStore diskStore) {
        this.diskStore = diskStore;
    }

    public MemoryStore(BitmapStore bitmapStore, Context context) {
        this.bitmapStore = bitmapStore;
        this.diskStore = bitmapStore.getDiskStore();
    }

    public MemoryStore(BitmapStore bitmapStore) {
        this.bitmapStore = bitmapStore;
        this.diskStore = bitmapStore.getDiskStore();
    }

    public abstract Bitmap load(Key key, Request request);

    public abstract byte[] loadFromDisk(Key key, Request request);

    protected abstract boolean existInRepo(Key key);

    public abstract void clear();
}
