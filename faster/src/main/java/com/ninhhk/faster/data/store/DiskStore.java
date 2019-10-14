package com.ninhhk.faster.data.store;

import com.ninhhk.faster.Callback;
import com.ninhhk.faster.Key;

public abstract class DiskStore {
    protected Callback<byte[]> callback;

    public void setCallback(Callback<byte[]> callback) {
        this.callback = callback;
    }

    public abstract byte[] load(Key key);

    protected abstract byte[] loadFromDataSource(Key key);

    protected abstract boolean existInRepo(Key key);
}
