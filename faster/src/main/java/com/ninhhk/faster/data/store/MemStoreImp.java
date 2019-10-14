package com.ninhhk.faster.data.store;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.ninhhk.faster.Callback;
import com.ninhhk.faster.decoder.ImageDecoder;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.decoder.MatchTargetDimensionImageDecoder;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.RequestManager;
import com.ninhhk.faster.RequestOption;

import java.util.Objects;

public class MemStoreImp extends MemoryStore {

    private static final int MAX_SIZE = 1024;
    private LruCache<Key, Bitmap> memCache;
    private ImageDecoder imageDecoder;
    private RequestManager requestManager;

    public MemStoreImp(DiskStore diskStore) {
        super(diskStore);
        memCache = new LruCache<>(MAX_SIZE);
        imageDecoder = new MatchTargetDimensionImageDecoder();
        requestManager = RequestManager.getInstance();
    }

    @Override
    public Bitmap load(Key key) {
        Objects.requireNonNull(this.callback);
        Bitmap bm = memCache.get(key);

        if (existInRepo(key)){
            callback.onReady(bm);
            return null;
        }
        loadFromDisk(key);
        return null;
    }

    @Override
    public byte[] loadFromDisk(Key key) {

        Callback<byte[]> callback = bytes -> {
            Bitmap bitmap = decodeFromBytes(bytes, key);
            saveToMemCache(key, bitmap);
            MemStoreImp.this.callback.onReady(bitmap);
        };

        diskStore.setCallback(callback);
        diskStore.load(key);
        return new byte[0];
    }

    private void saveToMemCache(Key key, Bitmap bitmap) {
        this.memCache.put(key, bitmap);
    }

    private Bitmap decodeFromBytes(byte[] bytes, Key key) {
        Request request = requestManager.getRequest(key);
        RequestOption requestOption = request.getRequestOption();

        Bitmap bitmap;
        bitmap = imageDecoder.decode(bytes, requestOption);
        return bitmap;
    }

    @Override
    protected boolean existInRepo(Key key) {
        return memCache.get(key) != null;
    }
}
