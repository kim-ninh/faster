package com.ninhhk.faster.utils;

import androidx.annotation.NonNull;

import com.ninhhk.faster.data.store.ByteBufferPool;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class StreamUtils {

    @NonNull
    public static ByteBuffer readToBuffer(InputStream is) throws IOException {
        int chunk_size = 1024;
        int nByteRead;

        ByteBuffer byteBuffer = null;
        try {
            byteBuffer = ByteBufferPool
                    .getInstance(MemoryUtils.BUFFER_CAPACITY)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (byteBuffer == null){
            return ByteBuffer.allocate(0);
        }
        byte[] bytes = new byte[chunk_size];

        do {
            nByteRead = is.read(bytes, 0, chunk_size);
            if (nByteRead == -1)
                break;

            byteBuffer.put(bytes, 0 , nByteRead);
        }while (true);

        return byteBuffer;
    }
}
