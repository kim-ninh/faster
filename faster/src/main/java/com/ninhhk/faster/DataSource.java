package com.ninhhk.faster;

import java.util.Objects;

public abstract class DataSource<T> {
    protected T model;

    protected ResponseDelegate<byte[]> byteLoad;

    public DataSource(T model) {
        this.model = model;
    }

    public void setByteLoadSuccess(ResponseDelegate<byte[]> byteLoad) {
        this.byteLoad = byteLoad;
    }

    abstract public void load();

    @Override
    public int hashCode() {
        return model.hashCode();
    }
}
