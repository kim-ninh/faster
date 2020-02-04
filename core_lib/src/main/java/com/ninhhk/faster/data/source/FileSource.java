package com.ninhhk.faster.data.source;

import android.content.Context;

import com.ninhhk.faster.Request;
import com.ninhhk.faster.data.store.ByteBufferPool;
import com.ninhhk.faster.utils.ExifUtils;
import com.ninhhk.faster.utils.MemoryUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class FileSource extends DataSource<File> {
    public FileSource(File model) {
        super(model);
        exifOrientation = ExifUtils.getImageRotation(model);
    }

    @Override
    public byte[] load(Context context, Request request) {
        try {

            request.exifOrientation = exifOrientation;
            FileInputStream fileInputStream = new FileInputStream(model);
            return readFromStream(fileInputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public InputStream getInputStream(Context context) {
        return null;
    }

    @Override
    public String name() {
        return model.getAbsolutePath();
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
