package com.ninhhk.faster.pool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ninhhk.faster.data.store.ByteBufferPool;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

class RecyclableByteBuffer {

    private static final String ILLEGAL_ACCESS_MESSAGE = "This buffer has recycle";
    @Nullable
    private ByteBuffer byteBuffer;
    private static ByteBufferPool pool;

    public static void initPool(@NonNull ByteBufferPool pool) {
        RecyclableByteBuffer.pool = pool;
    }

    @NonNull
    public synchronized static RecyclableByteBuffer takeFromPool()
            throws InterruptedException {
        ByteBuffer byteBuffer = pool.get();
        return new RecyclableByteBuffer(byteBuffer);
    }

    private RecyclableByteBuffer(@NonNull ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public void recycle()
            throws
            InterruptedException,
            IllegalAccessException {

        ByteBuffer byteBuffer = getInternalBuffer();
        pool.put(byteBuffer);
        this.byteBuffer = null;
    }

    @NonNull
    private ByteBuffer getInternalBuffer()
            throws IllegalAccessException {
        if (byteBuffer == null) {
            throw new IllegalAccessException(ILLEGAL_ACCESS_MESSAGE);
        }
        return byteBuffer;
    }

    @NonNull
    public RecyclableByteBuffer slice()
            throws IllegalAccessException {
        return new RecyclableByteBuffer(getInternalBuffer().slice());
    }

    @NonNull
    public RecyclableByteBuffer duplicate()
            throws IllegalAccessException {
        return new RecyclableByteBuffer(getInternalBuffer().duplicate());
    }

    @NonNull
    public RecyclableByteBuffer asReadOnlyBuffer()
            throws IllegalAccessException {
        return new RecyclableByteBuffer(getInternalBuffer().asReadOnlyBuffer());
    }

    public byte get()
            throws IllegalAccessException {
        return getInternalBuffer().get();
    }

    @NonNull
    public RecyclableByteBuffer put(byte b)
            throws IllegalAccessException {
        getInternalBuffer().put(b);
        return this;
    }


    public byte get(int index) throws IllegalAccessException {
        return getInternalBuffer().get(index);
    }

    @NonNull
    public RecyclableByteBuffer put(int index, byte b)
            throws IllegalAccessException {
        getInternalBuffer().put(index, b);
        return this;
    }

    @NonNull
    public RecyclableByteBuffer compact()
            throws IllegalAccessException {
        getInternalBuffer().compact();
        return this;
    }


    public boolean isReadOnly()
            throws IllegalAccessException {
        return getInternalBuffer().isReadOnly();
    }


    public boolean isDirect()
            throws IllegalAccessException {
        return getInternalBuffer().isDirect();
    }


    public char getChar()
            throws IllegalAccessException {
        return getInternalBuffer().getChar();
    }

    @NonNull
    public RecyclableByteBuffer putChar(char value)
            throws IllegalAccessException {
        getInternalBuffer().putChar(value);
        return this;
    }


    public char getChar(int index)
            throws IllegalAccessException {
        return getInternalBuffer().getChar(index);
    }

    @NonNull
    public RecyclableByteBuffer putChar(int index, char value)
            throws IllegalAccessException {
        getInternalBuffer().putChar(index, value);
        return this;
    }

    @NonNull
    public CharBuffer asCharBuffer()
            throws IllegalAccessException {
        return getInternalBuffer().asCharBuffer();
    }


    public short getShort()
            throws IllegalAccessException {
        return getInternalBuffer().getShort();
    }

    @NonNull
    public RecyclableByteBuffer putShort(short value)
            throws IllegalAccessException {
        getInternalBuffer().putShort(value);
        return this;
    }


    public short getShort(int index)
            throws IllegalAccessException {
        return getInternalBuffer().getShort(index);
    }

    @NonNull
    public RecyclableByteBuffer putShort(int index, short value)
            throws IllegalAccessException {
        getInternalBuffer().putShort(index, value);
        return this;
    }

    @NonNull
    public ShortBuffer asShortBuffer()
            throws IllegalAccessException {
        return getInternalBuffer().asShortBuffer();
    }


    public int getInt() throws IllegalAccessException {
        return getInternalBuffer().getInt();
    }

    @NonNull
    public RecyclableByteBuffer putInt(int value)
            throws IllegalAccessException {
        getInternalBuffer().putInt(value);
        return this;
    }


    public int getInt(int index) throws IllegalAccessException {
        return getInternalBuffer().getInt(index);
    }

    @NonNull
    public RecyclableByteBuffer putInt(int index, int value)
            throws IllegalAccessException {
        getInternalBuffer().putInt(index, value);
        return this;
    }

    @NonNull
    public IntBuffer asIntBuffer() throws IllegalAccessException {
        return getInternalBuffer().asIntBuffer();
    }


    public long getLong() throws IllegalAccessException {
        return getInternalBuffer().getLong();
    }

    @NonNull
    public RecyclableByteBuffer putLong(long value)
            throws IllegalAccessException {
        getInternalBuffer().putLong(value);
        return this;
    }


    public long getLong(int index) throws IllegalAccessException {
        return getInternalBuffer().getLong(index);
    }

    @NonNull
    public RecyclableByteBuffer putLong(int index, long value)
            throws IllegalAccessException {
        getInternalBuffer().putLong(index, value);
        return this;
    }

    @NonNull
    public LongBuffer asLongBuffer() throws IllegalAccessException {
        return getInternalBuffer().asLongBuffer();
    }


    public float getFloat() throws IllegalAccessException {
        return getInternalBuffer().getFloat();
    }

    @NonNull
    public RecyclableByteBuffer putFloat(float value)
            throws IllegalAccessException {
        getInternalBuffer().putFloat(value);
        return this;
    }


    public float getFloat(int index) throws IllegalAccessException {
        return getInternalBuffer().getFloat(index);
    }

    @NonNull
    public RecyclableByteBuffer putFloat(int index, float value)
            throws IllegalAccessException {
        getInternalBuffer().putFloat(index, value);
        return this;
    }

    @NonNull
    public FloatBuffer asFloatBuffer() throws IllegalAccessException {
        return getInternalBuffer().asFloatBuffer();
    }


    public double getDouble() throws IllegalAccessException {
        return getInternalBuffer().getDouble();
    }

    @NonNull
    public RecyclableByteBuffer putDouble(double value)
            throws IllegalAccessException {
        getInternalBuffer().putDouble(value);
        return this;
    }


    public double getDouble(int index) throws IllegalAccessException {
        return getInternalBuffer().getDouble(index);
    }

    @NonNull
    public RecyclableByteBuffer putDouble(int index, double value)
            throws IllegalAccessException {
        getInternalBuffer().putDouble(index, value);
        return this;
    }

    @NonNull
    public DoubleBuffer asDoubleBuffer() throws IllegalAccessException {
        return getInternalBuffer().asDoubleBuffer();
    }
}
