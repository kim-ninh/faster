package com.ninhhk.faster.data.store;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.util.LruCache;
import android.util.SparseIntArray;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.RequestOption;
import com.ninhhk.faster.StringUtils;
import com.ninhhk.faster.decoder.ImageDecoder;
import com.ninhhk.faster.pool.FasterBitmapPool;
import com.ninhhk.faster.transformer.Transformation;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.MemoryUtils;

import java.nio.ByteBuffer;

public class LruMemoryStore extends MemoryStore {

    public static final String TAG = LruMemoryStore.class.getSimpleName();

    private final int MAX_SIZE;

    private LruCache<Key, Bitmap> memCache;
    private FasterBitmapPool pool = FasterBitmapPool.getInstance();
    private SparseIntArray orientationTags = new SparseIntArray();


    public LruMemoryStore(BitmapStore bitmapStore, Context context) {
        super(bitmapStore);

        MAX_SIZE = getSuitableSize(context);
        memCache = new LruCache<Key, Bitmap>(MAX_SIZE) {
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

            synchronized (this) {
                request.orientationTag = orientationTags.get(key.hashCode(), ExifInterface.ORIENTATION_UNDEFINED);
            }

            Log.i(TAG, "Bitmap with key " + key.toString() + "has read from mem");
        } else {

            ByteBuffer byteBuffer = loadFromDisk(key, request);
            bm = decodeBitmapAndSave(key, byteBuffer, request.getRequestOption(), request.getImageDecoder());

            try {
                ByteBufferPool.getInstance(MemoryUtils.BUFFER_CAPACITY)
                        .put(byteBuffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this) {
                orientationTags.put(key.hashCode(), request.orientationTag);
            }


        }
        bm = applyExifOrientation(bm, request.orientationTag, request.getRequestOption());
        bm = applyTransformation(bm, request.getRequestOption());
        return bm;
    }

    private Bitmap applyExifOrientation(Bitmap bitmap,
                                        int orientationTag, RequestOption requestOption) {
        Matrix matrix = ExifUtils.getTransformMatrix(orientationTag);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
    }

    private Bitmap applyTransformation(Bitmap bm, RequestOption requestOption) {
        Transformation transformation = requestOption.getTransformation();
        return transformation.transform(bm, requestOption.getFinalWidth(), requestOption.getFinalHeight());
    }

    @NonNull
    private Bitmap decodeBitmapAndSave(@NonNull Key key,
                                       @NonNull ByteBuffer byteBuffer,
                                       @NonNull RequestOption requestOpts,
                                       @NonNull ImageDecoder decoder){

        Bitmap bitmap =  decodeFromByteBuffer(byteBuffer, decoder, requestOpts);
        saveToMemCache(key, bitmap);
        return bitmap;
    }

    @NonNull
    @Override
    protected ByteBuffer loadFromDisk(@NonNull Key key, @NonNull Request request) {
        return diskStore.loadToBuffer(key, request);
    }

    private void saveToMemCache(Key key, Bitmap bitmap) {
        this.memCache.put(key, bitmap);
        LogUtils.i(TAG, StringUtils.concat("Bitmap with key ", key.toString(), " has been cached!"));
    }

    @NonNull
    private Bitmap decodeFromByteBuffer(@NonNull ByteBuffer byteBuffer,
                                        @NonNull ImageDecoder decoder,
                                        @NonNull RequestOption requestOption){
        Bitmap bitmap;
        bitmap = decoder.decode(byteBuffer, requestOption);
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
