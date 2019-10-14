package com.ninhhk.faster.data.store;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.ninhhk.faster.Callback;
import com.ninhhk.faster.Key;

import java.util.Objects;

public class BitmapStore implements Callback<Bitmap> {
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private MemoryStore memoryRepo;
    private DiskStore diskStore;
    private Callback<Bitmap> callback;

    public BitmapStore() {
        this.diskStore = new DiskStoreImp();
        this.memoryRepo = new MemStoreImp(diskStore);
        memoryRepo.setCallback(this);
    }

    public Bitmap load(Key key){
        Objects.requireNonNull(callback);

        memoryRepo.load(key);
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
}
