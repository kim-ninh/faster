package com.ninhhk.faster.data.source;

import android.content.Context;

import androidx.annotation.Nullable;

import com.ninhhk.faster.Request;

import java.io.InputStream;

public abstract class DataSource<T> {
    protected T model;

    protected int exifOrientation = 0;

    public DataSource(T model) {
        this.model = model;
    }

    abstract public byte[] load(Context context, Request request);

    abstract public InputStream getInputStream(Context context);

    abstract public String name();

    @Override
    public int hashCode() {
        return model.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof DataSource<?>) {
            DataSource<?> dataSource = (DataSource<?>) obj;

            return dataSource.model.equals(model);
        }
        return false;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "model=" + model +
                '}';
    }
}
