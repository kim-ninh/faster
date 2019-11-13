package com.ninhhk.faster.data.store;

import android.content.Context;
import android.graphics.Bitmap;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;

public class BitmapStore {
    private MemoryStore memoryStore;
    private DiskStore diskStore;

    public BitmapStore(Context context) {
        this.diskStore = new LruDiskStore(this, context);   // need context for loading from Res, file
        this.memoryStore = new LruMemoryStore(this, context);  // need context for calculate for memory size
    }

    public Bitmap load(Key key, Request request) {

        return memoryStore.load(key, request);
    }

    public DiskStore getDiskStore() {
        return diskStore;
    }

    public MemoryStore getMemoryStore(){
        return memoryStore;
    }

    public void clearCache() {
        memoryStore.clear();
        diskStore.clear();
    }
}
