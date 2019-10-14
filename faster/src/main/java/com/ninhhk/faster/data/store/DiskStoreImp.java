package com.ninhhk.faster.data.store;

import com.ninhhk.faster.Callback;
import com.ninhhk.faster.data.source.DataSource;
import com.ninhhk.faster.Key;
import com.ninhhk.faster.Request;
import com.ninhhk.faster.RequestManager;

import java.io.File;
import java.util.Objects;

public class DiskStoreImp extends DiskStore {
    private final String DIR = "/faster";
    private RequestManager requestManager = RequestManager.getInstance();

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
        File file = new File(key.toString());
        return file.exists();
    }

    private void saveToDisk(Key key, byte[] bytes) {

    }

    private byte[] openFileWithKey(Key key) {


        return new byte[0];
    }
}
