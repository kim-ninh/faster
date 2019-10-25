package com.ninhhk.faster.data.store;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.ninhhk.faster.Callback;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.decoder.ImageDecoder;

public abstract class MemoryStore {

    protected Context context;
    protected DiskStore diskStore;
    protected Callback<Bitmap> callback;

    protected BitmapStore bitmapStore;

    public MemoryStore(DiskStore diskStore) {
        this.diskStore = diskStore;
    }

    public MemoryStore(BitmapStore bitmapStore, Context context) {
        this.bitmapStore = bitmapStore;
        this.context = context;
        setCallback(bitmapStore);
        this.diskStore = bitmapStore.getDiskStore();
    }

    public void setCallback(Callback<Bitmap> callback) {
        this.callback = callback;
    }


    public abstract Bitmap load(Key key);

    public abstract byte[] loadFromDisk(Key key);

    protected abstract boolean existInRepo(Key key);

}
