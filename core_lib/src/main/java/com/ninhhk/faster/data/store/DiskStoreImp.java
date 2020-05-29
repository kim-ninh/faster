package com.ninhhk.faster.data.store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    public DiskStoreImp(@NonNull File cacheBaseDir) {
        super(cacheBaseDir);
    }


    private void initSubDir() {
        if (! cacheDir.exists()){
            cacheDir.mkdir();
        }
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

    @Override
    public void cache(@NonNull Key key, @NonNull ByteBuffer data) {

    }

    @Nullable
    @Override
    public ByteBuffer load(@NonNull Key key, @NonNull Request request) {
        return null;
    }
}
