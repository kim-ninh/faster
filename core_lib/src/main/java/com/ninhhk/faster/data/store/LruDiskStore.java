package com.ninhhk.faster.data.store;

import android.content.Context;

import androidx.annotation.NonNull;

import com.jakewharton.disklrucache.DiskLruCache;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.StringUtils;
import com.ninhhk.faster.data.source.DataSource;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.StreamUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class LruDiskStore extends DiskStore {

    public static final String TAG = LruDiskStore.class.getSimpleName();

    private static final String DIR = "faster";
    private static final int MAX_BUFFER_IN_MB = 200;
    private static final int SINGLE_FILE_BUFFER_IN_MB = 7;
    private static final int MAX_BUFFER_IN_BYTE = SINGLE_FILE_BUFFER_IN_MB * 1024 * 1024;
    private final int appVersion = 1;
    private final int valueCount = 1;
    private final long CACHE_SIZE = 1024 * 1024 * MAX_BUFFER_IN_MB;
    private DiskLruCache diskLruCache;


    public LruDiskStore(BitmapStore bitmapStore, Context context) {
        super(bitmapStore, context);
    }

    private synchronized void openIfClose() {
        if (! config.isUseDiskCache()){
            return;
        }

        if (diskLruCache != null && !diskLruCache.isClosed())
            return;

        try {
            diskLruCache = DiskLruCache.open(cacheDir, appVersion, valueCount, CACHE_SIZE);
            LogUtils.i(TAG, StringUtils.concat("Cache dir has created in: ", diskLruCache.getDirectory().getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    ByteBuffer loadToBuffer(@NonNull Key key, @NonNull Request request) {

        ByteBuffer byteBuffer;

        openIfClose();

        if (config.isUseDiskCache() && exists(key)) {
            byteBuffer = readFileWithKey(key, request);

            return byteBuffer;
        }

        byteBuffer = loadDataSource(key, request.getDataSource(), request);
        return byteBuffer;
    }

    private ByteBuffer readFileWithKey(@NonNull Key key,
                                       @NonNull Request request){
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        String fileName = getFileName(key);
        BufferedInputStream is;
        DiskLruCache.Snapshot snapshot = null;

        try {
            snapshot = diskLruCache.get(fileName);

            is = new BufferedInputStream(snapshot.getInputStream(0));
            is.mark(0);
            request.orientationTag = ExifUtils.getOrientationTag(is);

            is.reset();
            byteBuffer = StreamUtils.readToBuffer(is);
            is.close();
            LogUtils.i(TAG, "Disk store has read " +
                    byteBuffer.position() + " bytes from file " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteBuffer;
    }

    private String getFileName(Key key) {

        String fileName = String.valueOf(key.hashCode());
        return fileName;
    }

    @NonNull
    @Override
    protected ByteBuffer loadDataSource(@NonNull Key key,
                                        @NonNull DataSource<?> dataSource,
                                        @NonNull Request request) {
        ByteBuffer byteBuffer;

        byteBuffer = dataSource.loadToBuffer(context, request);
        if (config.isUseDiskCache()){
            saveToDisk(key, byteBuffer);
        }

        return byteBuffer;
    }


    private void saveToDisk(Key key, ByteBuffer byteBuffer){
        String fileName = getFileName(key);
        DiskLruCache.Editor editor = null;
        OutputStream os;
        try {
            byte[] bytes = byteBuffer.array();
            int length = byteBuffer.position();

            editor = diskLruCache.edit(fileName);
            os = editor.newOutputStream(0);
            os.write(bytes, 0, length);
            editor.commit();

            os.close();
            LogUtils.i(TAG, "Saved " + length + " bytes to file " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean exists(Key key) {
        String keyName = getFileName(key);
        DiskLruCache.Snapshot snapshot = null;

        try {
            snapshot = diskLruCache.get(keyName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return snapshot != null;
    }

    @Override
    public void clear() {
        if (! config.isUseDiskCache()){
            return;
        }

        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
