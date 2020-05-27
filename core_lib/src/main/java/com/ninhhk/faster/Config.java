package com.ninhhk.faster;

import com.ninhhk.faster.utils.MemoryUtils;

public class Config {
    private boolean mUseDiskCache = true;
    private int mByteBufferCapacity = MemoryUtils.BUFFER_CAPACITY;

    private Config(Builder builder){
        this.mUseDiskCache = builder.mUseDiskCache;
        this.mByteBufferCapacity = builder.mByteBufferCapacity;
    }

    public boolean isUseDiskCache(){
        return mUseDiskCache;
    }

    public int getByteBufferCapacity(){
        return mByteBufferCapacity;
    }

    public static class Builder{
        private boolean mUseDiskCache = true;
        private int mByteBufferCapacity = MemoryUtils.BUFFER_CAPACITY;

        public Builder setUseDiskCache(boolean useDiskCache){
            mUseDiskCache = useDiskCache;
            return this;
        }

        public Builder setByteBufferCapacity(int byteBufferCapacity){
            mByteBufferCapacity = byteBufferCapacity;
            return this;
        }

        public Config build(){
            return new Config(this);
        }
    }
}
