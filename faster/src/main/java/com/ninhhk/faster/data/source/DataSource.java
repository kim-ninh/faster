package com.ninhhk.faster.data.source;

import com.ninhhk.faster.Callback;

public abstract class DataSource<T> {
    protected T model;

    protected Callback<byte[]> byteLoad;

    public DataSource(T model) {
        this.model = model;
    }

    public void setByteLoadSuccess(Callback<byte[]> byteLoad) {
        this.byteLoad = byteLoad;
    }

    abstract public void load();

    abstract public String name();

    @Override
    public int hashCode() {
        return model.hashCode();
    }
}
