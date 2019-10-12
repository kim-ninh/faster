package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.util.Objects;

public class BitmapRepo implements Callback<Bitmap>{
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private MemoryRepo memoryRepo;
    private DiskRepo diskRepo;
    private Callback<Bitmap> callback;

    public BitmapRepo() {
        this.diskRepo = new DiskImp();
        this.memoryRepo = new MemImp(diskRepo);
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
