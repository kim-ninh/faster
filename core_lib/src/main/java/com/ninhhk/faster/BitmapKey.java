package com.ninhhk.faster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ninhhk.faster.data.source.DataSource;

public class BitmapKey implements Key {
    private static final int PRIME_NUM = 2;
    private RequestOption requestOption;
    private int hash = 0;

    private DataSource<?> dataSource;
    private int width;
    private int height;

    public BitmapKey(DataSource<?> dataSource, int width, int height) {
        this.dataSource = dataSource;
        this.width = width;
        this.height = height;
    }

    public BitmapKey(DataSource<?> dataSource, RequestOption requestOption){
        this.dataSource = dataSource;
        this.requestOption = requestOption;
        this.width = requestOption.getFinalWidth();
        this.height = requestOption.getFinalHeight();
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof BitmapKey){
            BitmapKey key = (BitmapKey) obj;

            return key.dataSource.equals(dataSource)
                    && key.width == width
                    && key.height == height;
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (hash == 0){
            hash = dataSource.hashCode();
            hash = hash * PRIME_NUM + Integer.valueOf(width).hashCode();
            hash = hash * PRIME_NUM + Integer.valueOf(height).hashCode();
        }
        return hash;
    }

    @NonNull
    @Override
    public String toString() {
        return dataSource.toString() + "_" + width + "_" + height;
    }
}
