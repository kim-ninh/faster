package com.ninhhk.faster;

public interface ResponseDelegate<T> {
    void onReady(T data);
}
