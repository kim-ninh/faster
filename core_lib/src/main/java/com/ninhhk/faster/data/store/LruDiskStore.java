package com.ninhhk.faster.data.store;

import android.content.Context;

import com.jakewharton.disklrucache.DiskLruCache;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.StringUtils;
import com.ninhhk.faster.data.source.DataSource;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.MemoryUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

//    private static final ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_BUFFER_IN_BYTE);
//    private static final byte[] bytes = new byte[1024];

//    private static final ByteBuffer byteBuffer = null;
//    private static final byte[] bytes = null;


    public LruDiskStore(BitmapStore bitmapStore, Context context) {
        super(bitmapStore, context);
        openIfClose();
    }

    private void openIfClose() {
        File directory = getDir();

        if (diskLruCache != null && !diskLruCache.isClosed())
            return;

        try {
            diskLruCache = DiskLruCache.open(directory, appVersion, valueCount, CACHE_SIZE);
            LogUtils.i(TAG, StringUtils.concat("Cache dir has created in: ", diskLruCache.getDirectory().getAbsolutePath()));
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

        openIfClose();

        if (exists(key)) {
            bytes = openFileWithKey(key, request);

            return bytes;
        }

        bytes = loadFromDataSource(key, request.getDataSource(), request);
        return bytes;
    }

    @Override
    public InputStream getInputStream(Key key, Request request) {

        InputStream is = null;
        openIfClose();

        if (exists(key)) {
            is = getFileInputStreamWithKey(key);
        } else {
            InputStream fileInputStream = null;

            try {
                is = getInputStreamFromDataSource(key, request.getDataSource());
                fileInputStream = writeToFile(key, is);
                is.close();
                is = fileInputStream;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // make sure the InputStream is supported mark() & reset()
        is = new BufferedInputStream(is, MAX_BUFFER_IN_BYTE);
        is.mark(MAX_BUFFER_IN_BYTE);
        return is;
    }

    private InputStream getFileInputStreamWithKey(Key key) {
        InputStream is = null;
        String fileName = getFileName(key);
        DiskLruCache.Snapshot snapshot = null;

        try {
            snapshot = diskLruCache.get(fileName);
            is = snapshot.getInputStream(0);
            LogUtils.i(TAG, StringUtils.concat("File ", fileName, " has read from disk"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return is;
    }

    private byte[] openFileWithKey(Key key, Request request) {
        byte[] bytes = null;
        String fileName = getFileName(key);
        InputStream is;
        DiskLruCache.Snapshot snapshot = null;

        try {
            snapshot = diskLruCache.get(fileName);

            is = snapshot.getInputStream(0);
            request.exifOrientation = ExifUtils.getImageRotation(is);
            is.close();

            is = snapshot.getInputStream(0);
            bytes = readFromStream(is);
            is.close();
            LogUtils.i(TAG, StringUtils.concat("File ", fileName, " has read from disk"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    private byte[] readFromStream(InputStream is) throws IOException {
        int MAX_BYTE_READ_WRITE = 1024;
        int nByteRead;

        ByteBuffer byteBuffer = ByteBufferPool.getInstance(MemoryUtils.BUFFER_CAPACITY)
                .get();
        byte[] bytes = new byte[MAX_BYTE_READ_WRITE];
//        byteBuffer.clear();

        do {
            nByteRead = is.read(bytes, 0, MAX_BYTE_READ_WRITE);
            if (nByteRead == -1)
                break;

            byteBuffer.put(bytes, 0, nByteRead);
        } while (true);
        return byteBuffer.array();
    }

    private String getFileName(Key key) {

        String fileName = String.valueOf(key.hashCode());
        return fileName;
    }

    @Override
    protected byte[] loadFromDataSource(Key key, DataSource<?> dataSource, Request request) {
        byte[] dataSourceBytes;

        dataSourceBytes = dataSource.load(context, request);
        saveToDisk(key, dataSourceBytes);
        return dataSourceBytes;
    }

    @Override
    protected InputStream getInputStreamFromDataSource(Key key, DataSource<?> dataSource) {
        return dataSource.getInputStream(context);
    }


    private InputStream writeToFile(Key key, InputStream dataSourceInputStream) {
        String fileName = getFileName(key);
        DiskLruCache.Editor editor = null;
        OutputStream os;
        InputStream fileInputStream = null;
        try {
            editor = diskLruCache.edit(fileName);
            os = editor.newOutputStream(0);
            writeToStream(dataSourceInputStream, os);
            editor.commit();

            os.close();

            LogUtils.i(TAG, StringUtils.concat("File ", fileName, " has saved to disk"));


            DiskLruCache.Snapshot snapshot;
            snapshot = diskLruCache.get(fileName);
            fileInputStream = snapshot.getInputStream(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileInputStream;
    }

    private void writeToStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        while (true) {
            bytes = is.read(buffer);
            if (bytes == -1)
                break;
            os.write(buffer);
        }
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
            LogUtils.i(TAG, StringUtils.concat("File ", fileName, " has saved to disk"));

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
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
