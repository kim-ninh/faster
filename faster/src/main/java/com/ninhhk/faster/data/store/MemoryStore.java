package com.ninhhk.faster.data.store;

import android.graphics.Bitmap;

import com.ninhhk.faster.Callback;
import com.ninhhk.faster.Key;

public abstract class MemoryStore {

    protected DiskStore diskStore;
    protected Callback<Bitmap> callback;

    public MemoryStore(DiskStore diskStore) {
        this.diskStore = diskStore;
    }

    public void setCallback(Callback<Bitmap> callback) {
        this.callback = callback;
    }


    public abstract Bitmap load(Key key);

    public abstract byte[] loadFromDisk(Key key);

    protected abstract boolean existInRepo(Key key);

}
