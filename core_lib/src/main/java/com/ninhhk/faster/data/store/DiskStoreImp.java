package com.ninhhk.faster.data.store;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.RequestManager;
import com.ninhhk.faster.data.source.DataSource;

import java.io.File;
import java.nio.ByteBuffer;

public class DiskStoreImp extends DiskStore {
    private static final int MAX_BUFFER_IN_MB = 4;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;
    public static final String TAG = DiskStoreImp.class.getSimpleName();

    private RequestManager requestManager = RequestManager.getInstance();

    public DiskStoreImp(BitmapStore bitmapStore, Context context) {
        super(bitmapStore, context);
        initSubDir();
    }

    private void initSubDir() {
        if (! cacheDir.exists()){
            cacheDir.mkdir();
        }
    }

    @NonNull
    @Override
    ByteBuffer loadToBuffer(@NonNull Key key, @NonNull Request request) {
        return ByteBuffer.allocate(0);
    }

    @NonNull
    @Override
    protected ByteBuffer loadDataSource(@NonNull Key key,
                                        @NonNull DataSource<?> dataSource,
                                        @NonNull Request request) {
        return ByteBuffer.allocate(0);
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

    private File getCacheFile(Key key) {
        Request request = requestManager.getRequest(key);
        DataSource<?> dataSource = request.getDataSource();
        String fileName = dataSource.name();

        return new File(cacheDir, fileName);
    }

}
