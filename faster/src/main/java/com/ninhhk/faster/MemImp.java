package com.ninhhk.faster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import androidx.annotation.Nullable;

import java.util.Objects;

public class MemImp extends MemoryRepo {

    private static final int MAX_SIZE = 1024;
    private LruCache<Key, Bitmap> memCache;
    private ImageDecoder imageDecoder;
    private RequestManager requestManager;

    public MemImp(DiskRepo diskRepo) {
        super(diskRepo);
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
            MemImp.this.callback.onReady(bitmap);
        };

        diskRepo.setCallback(callback);
        diskRepo.load(key);
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
