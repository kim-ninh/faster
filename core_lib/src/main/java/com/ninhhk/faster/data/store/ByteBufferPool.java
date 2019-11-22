package com.ninhhk.faster.data.store;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ByteBufferPool {
    // POOL_CAPACITY must be smaller or equal TOTAL_THREAD_AVAILABLE
    private static final int POOL_CAPACITY = 10;
    private static ByteBufferPool sByteByfferPool;

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
        if (sByteByfferPool == null) {
            synchronized (ByteBufferPool.class) {
                if (sByteByfferPool == null) {
                    sByteByfferPool = new ByteBufferPool(bufferCapacity);
                }
            }
        }
        return sByteByfferPool;
    }

    public synchronized ByteBuffer get() {
        ByteBuffer byteBuffer;
        if (byteBuffers.size() != 0) {
            try {
                byteBuffer = byteBuffers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            byteBuffer = ByteBuffer.allocate(bufferCapacity);
        }
        return byteBuffer;
    }


    public void put(byte[] unusedBytes) {
//        byteBuffers.add(ByteBuffer.wrap(unusedBytes));
        try {
            byteBuffers.put(ByteBuffer.wrap(unusedBytes));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
