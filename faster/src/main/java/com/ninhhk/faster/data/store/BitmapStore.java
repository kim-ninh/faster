package com.ninhhk.faster.data.store;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.ninhhk.faster.Callback;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.decoder.ImageDecoder;

import java.util.Objects;

public class BitmapStore implements Callback<Bitmap> {
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private MemoryStore memoryStore;
    private DiskStore diskStore;
    private Callback<Bitmap> callback;

    public BitmapStore(Context context) {
        this.diskStore = new DiskStoreImp(this, context);   // need context for loading from Res, file
        this.memoryStore = new MemStoreImp(this, context);  // need context for calculate for memory size
    }

    public Bitmap load(Key key){
        Objects.requireNonNull(callback);

        memoryStore.load(key);
        return null;
    }

    public void setCallback(Callback<Bitmap> callback) {
        this.callback = callback;
    }

    @Override
    public void onReady(Bitmap bitmap) {
        // call whatever the callback set from the mainThread
        mainThreadHandler.post(()->{
            callback.onReady(bitmap);
        });
    }

    public DiskStore getDiskStore() {
        return diskStore;
    }

    public MemoryStore getMemoryStore(){
        return memoryStore;
    }
}
