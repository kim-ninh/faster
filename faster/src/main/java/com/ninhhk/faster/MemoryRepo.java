package com.ninhhk.faster;

import android.graphics.Bitmap;

import java.util.Objects;

public abstract class MemoryRepo {

    protected DiskRepo diskRepo;
    protected Callback<Bitmap> callback;

    public MemoryRepo(DiskRepo diskRepo) {
        this.diskRepo = diskRepo;
    }

    public void setCallback(Callback<Bitmap> callback) {
        this.callback = callback;
    }


    public abstract Bitmap load(Key key);

    public abstract byte[] loadFromDisk(Key key);

    protected abstract boolean existInRepo(Key key);

}
