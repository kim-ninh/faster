package com.ninhhk.faster.data.source;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ContentSchemeUri extends UriSource {

    private static final int MAX_BUFFER_IN_MB = 7;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;

    public ContentSchemeUri(Uri model) {
        super(model);
    }

    @Override
    public byte[] load(Context context) {
        byte[] bytes = new byte[0];
        ContentResolver contentResolver = context.getContentResolver();
        InputStream is;

        try {
            is = contentResolver.openInputStream(model);
            bytes = readFromStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    public String name() {
        return "uri_" + model;
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
