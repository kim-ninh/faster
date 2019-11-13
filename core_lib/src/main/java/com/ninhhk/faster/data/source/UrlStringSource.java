package com.ninhhk.faster.data.source;

import android.content.Context;

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
}
