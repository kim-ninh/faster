package com.ninhhk.faster.data.store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ByteBufferPool {
    // POOL_CAPACITY must be smaller or equal TOTAL_THREAD_AVAILABLE
    private static final int POOL_CAPACITY = 10;
    private static ByteBufferPool sByteBufferPool;

    private BlockingQueue<ByteBuffer> byteBuffers;
    private int bufferCapacity;

    private ByteBufferPool(int bufferCapacity) {
        this.byteBuffers = new LinkedBlockingQueue<>(POOL_CAPACITY);
        this.bufferCapacity = bufferCapacity;

        for (int i = 0; i < POOL_CAPACITY; i++) {
            byteBuffers.add(ByteBuffer.allocate(bufferCapacity));
        }
    }

    public static ByteBufferPool getInstance(int bufferCapacity) {
        if (sByteBufferPool == null) {
            synchronized (ByteBufferPool.class) {
                if (sByteBufferPool == null) {
                    sByteBufferPool = new ByteBufferPool(bufferCapacity);
                }
            }
        }
        return sByteBufferPool;
    }

    @NonNull
    public synchronized ByteBuffer get()
            throws InterruptedException {
        ByteBuffer byteBuffer;
        if (byteBuffers.size() != 0) {
            byteBuffer = byteBuffers.take();
            byteBuffer.clear();
        } else {
            byteBuffer = ByteBuffer.allocate(bufferCapacity);
        }
        return byteBuffer;
    }


    public void put(@NonNull ByteBuffer byteBuffer)
            throws InterruptedException {
        byteBuffers.put(byteBuffer);
    }
}
