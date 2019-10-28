package com.ninhhk.faster.data.store;

import android.content.Context;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.data.source.DataSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyDiskStore extends DiskStore {

    public static final String TAG = MyDiskStore.class.getSimpleName();

    private static final String DIR = "faster";
    private static final int MAX_BUFFER_IN_MB = 4;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;
    private final int appVersion = 1;
    private final int valueCount = 1;
    private final long CACHE_SIZE = 1024 * 1024 * 10;
    private DiskLruCache diskLruCache;

    public MyDiskStore(BitmapStore bitmapStore, Context context) {
        super(bitmapStore, context);
        File directory = getDir();

        try {
            diskLruCache = DiskLruCache.open(directory, appVersion, valueCount, CACHE_SIZE);
            Log.i(TAG, "Cache dir has created in:" + diskLruCache.getDirectory().getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDir() {
        final String cachePath = cacheDir.getPath();
        File file = new File(cachePath + File.separator + DIR);
        return file;
    }

    @Override
    public byte[] load(Key key, Request request) {
        byte[] bytes;

        if (exists(key)) {
            bytes = openFileWithKey(key);

            return bytes;
        }

        bytes = loadFromDataSource(key, request.getDataSource());
        return bytes;
    }

    private byte[] openFileWithKey(Key key) {
        byte[] bytes = new byte[0];
        String fileName = getFileName(key);
        InputStream is;
        DiskLruCache.Snapshot snapshot = null;

        try {
            snapshot = diskLruCache.get(fileName);
            is = snapshot.getInputStream(0);
            bytes = readFromStream(is);
            is.close();
            Log.i(TAG, "File " + fileName + " has read from disk");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    private byte[] readFromStream(InputStream is) throws IOException {
        int MAX_BYTE_READ_WRITE = 1024;
        int nByteRead;
        ByteArrayOutputStream array = new ByteArrayOutputStream(MAX_BUFFER_IN_BYTE);

        byte[] bytes = new byte[MAX_BYTE_READ_WRITE];
        do {
            nByteRead = is.read(bytes, 0, MAX_BYTE_READ_WRITE);
            if (nByteRead == -1)
                break;

            array.write(bytes, 0, nByteRead);
        } while (true);

        return array.toByteArray();
    }

    private String getFileName(Key key) {

        String fileName = String.valueOf(key.hashCode());
        return fileName;
    }

    @Override
    protected byte[] loadFromDataSource(Key key, DataSource<?> dataSource) {
        byte[] dataSourceBytes;

        dataSourceBytes = dataSource.load(context);
        saveToDisk(key, dataSourceBytes);
        return dataSourceBytes;
    }

    private void saveToDisk(Key key, byte[] bytes) {
        String fileName = getFileName(key);
        DiskLruCache.Editor editor = null;
        OutputStream os;
        try {
            editor = diskLruCache.edit(fileName);
            os = editor.newOutputStream(0);
            os.write(bytes);
            editor.commit();

            os.close();
            Log.i(TAG, "File " + fileName + " has saved to disk");
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
}
