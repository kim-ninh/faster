package com.ninhhk.faster.data.store;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;

import java.io.InputStream;
import java.nio.ByteBuffer;

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

    @NonNull
    protected abstract ByteBuffer loadFromDisk(@NonNull Key key,
                                               @NonNull Request request);

    protected abstract boolean exists(Key key);

    public abstract void clear();
}
