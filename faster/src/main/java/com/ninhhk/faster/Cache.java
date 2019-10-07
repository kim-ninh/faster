package com.ninhhk.faster;

public interface Cache<K, V> {
    V get(K key);

    int size();

    void put(K key, V value);
}
