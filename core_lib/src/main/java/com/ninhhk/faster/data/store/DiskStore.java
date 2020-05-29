package com.ninhhk.faster.data.store;

import androidx.annotation.NonNull;

import com.ninhhk.faster.Cacheable;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.Loadable;

import java.io.File;
import java.nio.ByteBuffer;

public abstract class DiskStore
        implements Loadable<ByteBuffer>, Cacheable<ByteBuffer> {
    protected @NonNull final File cacheDir;

    public DiskStore(@NonNull File cacheBaseDir){
        cacheDir = new File(cacheBaseDir, "faster");
    }

    protected abstract boolean exists(Key key);

    public abstract void clear();
}
