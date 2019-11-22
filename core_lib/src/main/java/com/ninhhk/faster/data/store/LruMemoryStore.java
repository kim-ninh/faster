package com.ninhhk.faster.data.store;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.RequestOption;
import com.ninhhk.faster.StringUtils;
import com.ninhhk.faster.decoder.ImageDecoder;
import com.ninhhk.faster.pool.FasterBitmapPool;
import com.ninhhk.faster.transformer.Transformation;
import com.ninhhk.faster.utils.MemoryUtils;

import java.io.InputStream;

public class LruMemoryStore extends MemoryStore {

    public static final String TAG = LruMemoryStore.class.getSimpleName();

    private final int MAX_SIZE;

    private LruCache<Key, Bitmap> memCache;
    private FasterBitmapPool pool = FasterBitmapPool.getInstance();

    public LruMemoryStore(BitmapStore bitmapStore, Context context) {
        super(bitmapStore);

        MAX_SIZE = getSuitableSize(context);
        memCache = new LruCache<Key, Bitmap>(MAX_SIZE){
            @Override
            protected void entryRemoved(boolean evicted, Key key, Bitmap oldValue, Bitmap newValue) {

                // bitmap has rejected
                if (evicted && newValue == null){
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

    private static int getSuitableSize(Context context) {
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

    @Override
    public Bitmap load(Key key, Request request) {
        Bitmap bm = memCache.get(key);

        if (exists(key)) {
            request.isLoadForMem = true;
            Log.i(TAG, "Bitmap with key " + key.toString() + "has read from mem");
        }else{

            // use byte[] to load & decode image
            byte[] originBytes = loadFromDisk(key, request);
            Bitmap bitmap = decodeBitmapAndSave(key, originBytes, request.getRequestOption(), request.getImageDecoder());
            bm = bitmap;

            ByteBufferPool.getInstance(MemoryUtils.BUFFER_CAPACITY)
                    .put(originBytes);

            //use InputStream to load & decode image, not stable for UrlStringResource

//            InputStream bufferedInputStream;
//            Bitmap bitmap;
//            try {
//                bufferedInputStream = getStreamFromFile(key, request);
//                bitmap = decodeBitmapAndSave(key, bufferedInputStream, request.getRequestOption(), request.getImageDecoder());
//                bm = bitmap;
//                bufferedInputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }

        bm = applyTransformation(bm, request.getRequestOption());
        return bm;
    }

    private Bitmap applyTransformation(Bitmap bm, RequestOption requestOption) {
        Transformation transformation = requestOption.getTransformation();
        return transformation.transform(bm, requestOption.getFinalWidth(), requestOption.getFinalHeight());
    }

    private Bitmap decodeBitmapAndSave(Key key,
                                       byte[] originBytes,
                                       RequestOption requestOpts,
                                       ImageDecoder decoder) {
        Bitmap bitmap = decodeFromBytes(originBytes, decoder, requestOpts);
        saveToMemCache(key, bitmap);
        return bitmap;
    }

    private Bitmap decodeBitmapAndSave(Key key,
                                       InputStream is,
                                       RequestOption requestOption,
                                       ImageDecoder decoder) {
        Bitmap bitmap = decodeFromStream(is, decoder, requestOption);
        saveToMemCache(key, bitmap);
        return bitmap;
    }

    private Bitmap decodeFromStream(InputStream is, ImageDecoder decoder, RequestOption requestOption) {
        Bitmap bitmap;
        bitmap = decoder.decode(is, requestOption);
        return bitmap;
    }

    @Override
    protected byte[] loadFromDisk(Key key, Request request) {

        return diskStore.load(key, request);
    }

    @Override
    protected InputStream getStreamFromFile(Key key, Request request) {
        return diskStore.getInputStream(key, request);
    }

    private void saveToMemCache(Key key, Bitmap bitmap) {
        this.memCache.put(key, bitmap);
        LogUtils.i(TAG, StringUtils.concat("Bitmap with key ", key.toString(), " has been cached!"));
    }

    private Bitmap decodeFromBytes(byte[] bytes,
                                   ImageDecoder decoder,
                                   RequestOption requestOption) {

        Bitmap bitmap;
        bitmap = decoder.decode(bytes, requestOption);
        return bitmap;
    }

    @Override
    protected boolean exists(Key key) {
        return memCache.get(key) != null;
    }

    @Override
    public void clear() {
        memCache.evictAll();
    }
}
