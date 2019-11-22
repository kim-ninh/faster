package com.ninhhk.faster.data.source;

import android.content.Context;

import com.ninhhk.faster.data.store.ByteBufferPool;
import com.ninhhk.faster.utils.MemoryUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

public class UrlStringSource extends DataSource<String> {

    private static final String TAG = UrlStringSource.class.getSimpleName();
    private static final int MAX_BUFFER_IN_MB = 4;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;
//    private static final ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_BUFFER_IN_BYTE);
//    private static final ByteBuffer byteBuffer = null;
//    private static byte[] bytes = new byte[1024];

//    private ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_BUFFER_IN_BYTE);
//    private byte[] bytes = new byte[1024];

    public UrlStringSource(String model) {
        super(model);
    }

    @Override
    public byte[] load(Context context) {
        byte[] bytes = new byte[0];
        HttpURLConnection connection;
        InputStream is;
        try {
            URL url = new URL(UrlStringSource.this.model);
            connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bytes = readFromStream(is);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public InputStream getInputStream(Context context) {

        HttpURLConnection connection;
        InputStream is = null;
        try {
            URL url = new URL(UrlStringSource.this.model);
            connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    @Override
    public String name() {
        String url = model;
        String remoteFileName;
        int lastIndexOfSlash;

        lastIndexOfSlash = url.lastIndexOf('/');
        remoteFileName = url.substring(lastIndexOfSlash + 1);

        return remoteFileName;
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
}
