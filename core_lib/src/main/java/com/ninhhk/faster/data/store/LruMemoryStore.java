package com.ninhhk.faster.data.store;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.StringUtils;
import com.ninhhk.faster.pool.FasterBitmapPool;

public class LruMemoryStore extends MemoryStore {

    public static final String TAG = LruMemoryStore.class.getSimpleName();

    private LruCache<Key, Bitmap> memCache;
    private FasterBitmapPool pool = FasterBitmapPool.getInstance();

    public static int getSuitableSize(@NonNull Context context) {
        int memoryClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int maxSize;
        if (memoryClass >= 192) {
            maxSize = 30;
        } else {
            maxSize = 15;
        }
        int cacheSizeInMB = Math.min(maxSize, memoryClass / 7);
        int cacheSize = cacheSizeInMB * 1024 * 1024;
        Log.i(TAG, "getSuitableSize: " + cacheSizeInMB + " MB");
        return cacheSize;
    }

    public LruMemoryStore(int maxCacheSize) {
        super(maxCacheSize);
        memCache = new LruCache<Key, Bitmap>(maxCacheSize){
            @Override
            protected void entryRemoved(boolean evicted, Key key, Bitmap oldValue, Bitmap newValue) {
                // bitmap has rejected
                if (evicted && newValue == null) {
                    // put to bitmap pool
                    pool.put(oldValue);
                }
            }

            @Override
            protected int sizeOf(Key key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    private void saveToMemCache(Key key, Bitmap bitmap) {
        this.memCache.put(key, bitmap);
        LogUtils.i(TAG, StringUtils.concat("Bitmap with key ", key.toString(), " has been cached!"));
    }

    @Override
    public void clear() {
        memCache.evictAll();
    }

    @Override
    public void cache(@NonNull Key key, @NonNull Bitmap data) {
        saveToMemCache(key, data);
    }

    @Nullable
    @Override
    public Bitmap load(@NonNull Key key, @NonNull Request request) {
        Bitmap data = memCache.get(key);
        return data;
    }
}
