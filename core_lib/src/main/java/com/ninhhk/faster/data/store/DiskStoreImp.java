package com.ninhhk.faster.data.store;

import android.content.Context;
import android.util.Log;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.LogUtils;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.RequestManager;
import com.ninhhk.faster.StringUtils;
import com.ninhhk.faster.data.source.DataSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskStoreImp extends DiskStore {
    private static final int MAX_BUFFER_IN_MB = 4;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;
    public static final String TAG = DiskStoreImp.class.getSimpleName();

    private final String DIR = "faster";
    private RequestManager requestManager = RequestManager.getInstance();
    private File cacheDir;

    public DiskStoreImp(BitmapStore bitmapStore, Context context) {
        super(bitmapStore, context);
        cacheDir = initSubDir();
    }

    private File initSubDir() {
        final String cachePath = context.getCacheDir().getPath();
        File file = new File(cachePath + File.separator + DIR);
        if (!file.exists())
            file.mkdir();
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

    @Override
    public InputStream getInputStream(Key key, Request request) {
        return null;
    }

    // invoke callback.onReady(bytes) when the load is done
    @Override
    protected byte[] loadFromDataSource(Key key, DataSource<?> dataSource) {

        return dataSource.load(context);
    }

    @Override
    protected InputStream getInputStreamFromDataSource(Key key, DataSource<?> dataSource) {
        return null;
    }

    @Override
    protected boolean exists(Key key) {
        File file = getCacheFile(key);
        return file.exists();
    }

    @Override
    public void clear() {
        // delete file recursive
    }

    private void saveToDisk(Key key, byte[] bytes) {
        File file = getCacheFile(key);
        FileOutputStream fileOutputStream;

        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            writeToStream(fileOutputStream, bytes);
            fileOutputStream.close();

            Log.i(TAG, "File " + file.getAbsolutePath() + " saved!");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private File getCacheFile(Key key) {
        Request request = requestManager.getRequest(key);
        DataSource<?> dataSource = request.getDataSource();
        String fileName = dataSource.name();

        return new File(cacheDir, fileName);
    }

    private void writeToStream(OutputStream outputStream, byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    private byte[] openFileWithKey(Key key) {
        byte[] bytes = new byte[0];
        File file = getCacheFile(key);
        FileInputStream fileInputStream;

        try {
            fileInputStream = new FileInputStream(file);
            bytes = readFromStream(fileInputStream);
            fileInputStream.close();

            LogUtils.i(TAG, StringUtils.concat("File ", file.getAbsolutePath(), " read!"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    private byte[] readFromStream(InputStream inputStream) throws IOException {
        int MAX_BYTE_READ_WRITE = 1024;
        int nByteRead;
        ByteArrayOutputStream array = new ByteArrayOutputStream(MAX_BUFFER_IN_BYTE);

        byte[] bytes = new byte[MAX_BYTE_READ_WRITE];
        do {
            nByteRead = inputStream.read(bytes, 0, MAX_BYTE_READ_WRITE);
            if (nByteRead == -1)
                break;

            array.write(bytes, 0, nByteRead);
        } while (true);

        return array.toByteArray();
    }
}
