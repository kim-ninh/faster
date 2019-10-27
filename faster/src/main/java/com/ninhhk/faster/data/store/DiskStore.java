package com.ninhhk.faster.data.store;

import android.content.Context;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.data.source.DataSource;

public abstract class DiskStore {
    protected Context context;

    protected BitmapStore bitmapStore;

    public DiskStore(BitmapStore bitmapStore, Context context) {
        this.bitmapStore = bitmapStore;
        this.context = context;
    }

    public abstract byte[] load(Key key, Request request);

    protected abstract byte[] loadFromDataSource(Key key, DataSource<?> dataSource);

    protected abstract boolean exists(Key key);
}
