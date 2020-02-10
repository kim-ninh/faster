package com.ninhhk.faster.data.source;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.ninhhk.faster.Request;
import com.ninhhk.faster.data.store.ByteBufferPool;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.MemoryUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ContentSchemeUri extends UriSource {

    private static final int MAX_BUFFER_IN_MB = 7;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;
//    private static final ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_BUFFER_IN_BYTE);
//    private static final byte[] bytes = new byte[1024];

//    private static final ByteBuffer byteBuffer = null;
//    private static final byte[] bytes = null;

//    private ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_BUFFER_IN_BYTE);
//    private byte[] bytes = new byte[1024];


    public ContentSchemeUri(Uri model) {
        super(model);
    }

    @Override
    public byte[] load(Context context, Request request) {
        byte[] bytes = null;
        ContentResolver contentResolver = context.getContentResolver();
        InputStream is;
        try {
            is = contentResolver.openInputStream(model);
            if (is == null)
                return new byte[0];
            request.orientationTag = ExifUtils.getOrientationTag(is);
            is.close();

            is = contentResolver.openInputStream(model);
            if (is == null)
                return new byte[0];
            bytes = readFromStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    public InputStream getInputStream(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        InputStream is = null;

        try {
            is = contentResolver.openInputStream(model);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return is;
    }

    @Override
    public String name() {
        return "uri_" + model;
    }

    private byte[] readFromStream(InputStream is) throws IOException {
        int MAX_BYTE_READ_WRITE = 1024;
        int nByteRead;

//        byteBuffer.clear();

        ByteBuffer byteBuffer = ByteBufferPool.getInstance(MemoryUtils.BUFFER_CAPACITY).get();
        byte[] bytes = new byte[MAX_BYTE_READ_WRITE];

        do {
            nByteRead = is.read(bytes, 0, MAX_BYTE_READ_WRITE);
            if (nByteRead == -1)
                break;

            byteBuffer.put(bytes, 0, nByteRead);
        } while (true);

        return byteBuffer.array();
    }
}
