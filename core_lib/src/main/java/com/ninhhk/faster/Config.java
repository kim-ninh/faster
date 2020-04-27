package com.ninhhk.faster;

public class Config {
    private boolean mUseDiskCache = true;

    private Config(Builder builder){
        this.mUseDiskCache = builder.mUseDiskCache;
    }

    public boolean isUseDiskCache(){
        return mUseDiskCache;
    }

    public static class Builder{
        private boolean mUseDiskCache = true;

        public Builder setUseDiskCache(boolean useDiskCache){
            mUseDiskCache = useDiskCache;
            return this;
        }

        public Config build(){
            return new Config(this);
        }
    }
}
