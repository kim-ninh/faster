package com.ninhhk.faster.data.source;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlStringSource extends DataSource<String> {

    private static final String TAG = UrlStringSource.class.getSimpleName();
    private static final int MAX_BUFFER_IN_MB = 4;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;

    private Handler handler = new Handler(Looper.getMainLooper());

    public UrlStringSource(String model) {
        super(model);
    }

    @Override
    public void load() {

        Thread t = new Thread(() -> {
            HttpURLConnection connection;
            InputStream is;
            try {
                URL url = new URL(UrlStringSource.this.model);
                connection = (HttpURLConnection) url.openConnection();
                is = new BufferedInputStream(connection.getInputStream());
                readFromStream(is);
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
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

    private void readFromStream(InputStream is) throws IOException {
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

        byteLoad.onReady(array.toByteArray());
//        handler.post(() -> {
//            byteLoad.onReady(array.toByteArray());
//        });
    }
}
