package com.ninhhk.faster;

public abstract class DiskRepo {
    protected Callback<byte[]> callback;

    public void setCallback(Callback<byte[]> callback) {
        this.callback = callback;
    }

    public abstract byte[] load(Key key);

    protected abstract byte[] loadFromDataSource(Key key);

    protected abstract boolean existInRepo(Key key);
}
