package com.ninhhk.faster;

public interface Key {

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}
