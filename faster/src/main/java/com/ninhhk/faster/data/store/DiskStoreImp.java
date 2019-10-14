package com.ninhhk.faster.data.store;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ninhhk.faster.BitmapKey;
import com.ninhhk.faster.BitmapKeyFactory;
import com.ninhhk.faster.Callback;
import com.ninhhk.faster.KeyFactory;
import com.ninhhk.faster.data.source.DataSource;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.RequestManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import static com.ninhhk.faster.RequestOption.UNSET;

public class DiskStoreImp extends DiskStore {
    private static final int MAX_BUFFER_IN_MB = 4;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;
    public static final String TAG = DiskStoreImp.class.getSimpleName();

    private Context context;
    private final String DIR = "faster";
    private RequestManager requestManager = RequestManager.getInstance();
    private File cacheDir;

    public DiskStoreImp(Context context) {
        this.context = context;
        cacheDir = initSubDir();
    }

    private File initSubDir() {
        final String cachePath = context.getCacheDir().getPath();
        File file = new File(cachePath + File.separator + DIR);
        return file;
    }

    @Override
    public byte[] load(Key key) {
        Objects.requireNonNull(this.callback);
        byte[] bytes;

        if (existInRepo(key)) {
            bytes = openFileWithKey(key);

            this.callback.onReady(bytes);
            return bytes;
        }

        bytes = loadFromDataSource(key);
        return bytes;
    }

    // invoke callback.onReady(bytes) when the load is done
    @Override
    protected byte[] loadFromDataSource(Key key) {
        Request request = requestManager.getRequest(key);
        DataSource<?> dataSource = request.getDataSource();
        Callback<byte[]> callback = bytes -> {
            saveToDisk(key, bytes);
            DiskStoreImp.this.callback.onReady(bytes);
        };

        dataSource.setByteLoadSuccess(callback);
        dataSource.load();
        return new byte[0];
    }

    @Override
    protected boolean existInRepo(Key key) {
        File file = getCacheFile(key);
        return file.exists();
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

            Log.i(TAG, "File " + file.getAbsolutePath() + " read!");
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
