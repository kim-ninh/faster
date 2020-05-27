package com.ninhhk.faster.data.store;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ninhhk.faster.Config;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.data.source.DataSource;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;

public abstract class DiskStore {
    protected Context context;
    protected @NonNull final File cacheDir;
    protected BitmapStore bitmapStore;
    protected Config config;

    public DiskStore(BitmapStore bitmapStore, Context context) {
        this.bitmapStore = bitmapStore;
        cacheDir = new File(context.getCacheDir(), "faster");
        this.context = context;
    }

    @NonNull
    abstract ByteBuffer loadToBuffer(@NonNull Key key,
                                     @NonNull Request request);

    @NonNull
    protected abstract ByteBuffer loadDataSource(@NonNull Key key,
                                                 @NonNull DataSource<?> dataSource,
                                                 @NonNull Request request);

    protected abstract boolean exists(Key key);

    public abstract void clear();

    public void setFasterConfig(Config config){
        this.config = config;
    }
}
