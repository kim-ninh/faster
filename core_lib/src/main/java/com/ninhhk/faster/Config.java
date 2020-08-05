package com.ninhhk.faster;

import com.ninhhk.faster.utils.MemoryUtils;

public class Config {
    private boolean mUseDiskCache = true;
    private int mByteBufferCapacity = MemoryUtils.BUFFER_CAPACITY;
    private boolean mEnableLogging = false;

    private Config(Builder builder){
        this.mUseDiskCache = builder.mUseDiskCache;
        this.mByteBufferCapacity = builder.mByteBufferCapacity;
        this.mEnableLogging = builder.mEnableLogging;
    }

    public boolean isUseDiskCache(){
        return mUseDiskCache;
    }

    public int getByteBufferCapacity(){
        return mByteBufferCapacity;
    }

    public boolean isEnableLogging() {
        return mEnableLogging;
    }

    public static class Builder{
        private boolean mUseDiskCache = true;
        private int mByteBufferCapacity = MemoryUtils.BUFFER_CAPACITY;
        private boolean mEnableLogging = false;

        public Builder setUseDiskCache(boolean useDiskCache){
            mUseDiskCache = useDiskCache;
            return this;
        }

        public Builder setByteBufferCapacity(int byteBufferCapacity){
            mByteBufferCapacity = byteBufferCapacity;
            return this;
        }

        public Builder enableLogging(boolean enableLogging){
            mEnableLogging = enableLogging;
            return this;
        }

        public Config build(){
            return new Config(this);
        }
    }
}
